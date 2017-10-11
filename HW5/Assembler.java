//package simplecomputer;

import java.io.*;
import java.util.*;

public class Assembler {
 public static void main(String[] args) {
  System.out.print("Enter assembly file name:");
  Scanner kbd = new Scanner(System.in);
  String infile = kbd.next();
  System.out.print("Enter executable file name:");
  String outfile = kbd.next();
  kbd.close();
  try ( Reader rdr = new FileReader(infile);
    PrintWriter wtr = new PrintWriter(outfile)) {
   ProgramInfo info = readFile(rdr);  
   generateCode(info, wtr);           
  }
  catch(IOException ex) {
   System.out.println("file error");
   System.exit(0);
  }
  System.out.println("executable file created");
 }

 // PASS 1: Read the file, populate the symbol table.

 private static ProgramInfo readFile(Reader rdr) {
  ProgramInfo info = new ProgramInfo();
  int currentByte = 0;
  Scanner scanner = new Scanner(rdr);
  while (scanner.hasNextLine()) {
   String line = scanner.nextLine().trim().toLowerCase();  // to make the input file case insensitive
   if (line.length() == 0)
    continue;  // ignore empty lines
   if (line.charAt(0) == ';')
    continue; // ignore comment-only lines
   String[] inputTokens = line.split("\\s+");
   List<String> outputTokens = new ArrayList<>();
   outputTokens.add(""+currentByte);
   for (int i=0; i<inputTokens.length; i++) {
    String token = inputTokens[i];
    if (token.startsWith(";")) // ignore comments in rest of line
     break;
    else if (token.endsWith(":")) {
     String id = token.substring(0, token.length()-1); // remove the ":"
     info.symbols.put(id, currentByte); 
    }
    else if (token.equals("space")) { // process the line in this pass
     int size = Integer.parseInt(inputTokens[i+1]);
     currentByte += size;
     outputTokens = null;
     break; 
    }
    else 
     outputTokens.add(token);
   }
   if (outputTokens != null) {
    info.codelines.add(outputTokens);
    currentByte += 4; 
   }
  }
  scanner.close();
  return info;
 }

 // PASS 2: Generate machine-language instructions.

 private static void generateCode(ProgramInfo info, PrintWriter wtr) {
  for (List<String> L : info.codelines) {
   int machinecode = processInstruction(L, info.symbols);
   wtr.println(machinecode);
  }
 }

 private static int processInstruction(List<String> L, Map<String,Integer> symbols) {
  String op = L.get(1);
  if      (op.equals("system"))    return assembleSystem(0, L);
  else if (op.equals("compare"))   return assembleTwoRegisterOp(1, L);
  else if (op.equals("jump"))      return assembleJump(2, L, symbols);
  else if (op.equals("jumpeq"))    return assembleJump(3, L, symbols);
  else if (op.equals("jumpne"))    return assembleJump(4, L, symbols);
  else if (op.equals("jumplt"))    return assembleJump(5, L, symbols);
  else if (op.equals("jumple"))    return assembleJump(6, L, symbols);
  else if (op.equals("jumpgt"))    return assembleJump(7, L, symbols);
  else if (op.equals("jumpge"))    return assembleJump(8, L, symbols);
  else if (op.equals("setvalue"))  return assembleSetValue(9, L);  
  else if (op.equals("add"))       return assembleThreeRegisterOp(10, L);
  else if (op.equals("subtract"))  return assembleThreeRegisterOp(11, L);
  else if (op.equals("multiply"))  return assembleThreeRegisterOp(12, L);
  else if (op.equals("divide"))    return assembleThreeRegisterOp(13, L);
  else if (op.equals("mod"))       return assembleThreeRegisterOp(14, L);
  else if (op.equals("load"))      return assembleTwoRegisterOp(15, L);
  else if (op.equals("store"))     return assembleTwoRegisterOp(16, L);
  else if (op.equals("loadaddr"))  return assembleLoadAddress(9, L, symbols);
  else if (op.equals("jumpsub"))   return assembleJump(17, L, symbols);
  else if (op.equals("return"))    return assembleZeroRegisterOp(18);
  else if (op.equals("pushreg"))   return assembleOneRegisterOp(19, L);
  else if (op.equals("popreg"))    return assembleZeroRegisterOp(20);
  else if (op.equals("restore"))   return assembleOneRegisterOp(21, L);
  else if (op.equals("loadsbyte")) return assembleTwoRegisterOp(22, L);
  else if (op.equals("loadubyte")) return assembleTwoRegisterOp(23, L);
  else if (op.equals("storebyte")) return assembleTwoRegisterOp(24, L);
  else {
   System.out.println("invalid operation: " + op);
   return 0;
  }
 }

 private static int assembleSystem(int opcode, List<String> L) {
  int instruction = 0;
  instruction = embed(instruction, 0, 4, opcode);
  int syscall = 0;
  if (L.get(2).equals("read"))
    syscall = 1;
  else if (L.get(2).equals("write"))
    syscall = 2;
  instruction = embed(instruction, 5, 8, syscall);
  if (!L.get(2).equals("exit"))
    instruction = embed(instruction, 9, 12, getRegister(L.get(3)));
  return instruction;
 } 

 private static int assembleThreeRegisterOp(int opcode, List<String> L) {
  int instruction = 0;
  instruction = embed(instruction, 0,  4,  opcode);
  instruction = embed(instruction, 5,  8,  getRegister(L.get(2)));
  instruction = embed(instruction, 9, 12,  getRegister(L.get(3)));
  instruction = embed(instruction, 13, 16, getRegister(L.get(4))); 
  return instruction;
 }

 private static int assembleJump(int opcode, List<String> L, Map<String,Integer> labels) {
  int instruction = 0;
  instruction = embed(instruction, 0, 4, opcode);
  String label = L.get(2);
  int destination = labels.get(label);
  int myaddress = Integer.parseInt(L.get(0));
  int difference = (destination - myaddress) / 4;  //divide by 4 to get word difference
  instruction = embed(instruction, 5, 31, difference);   
  return instruction;
 }

 private static int assembleSetValue(int opcode, List<String> L) {
  int instruction = 0;
  instruction = embed(instruction, 0, 4, opcode);
  instruction = embed(instruction, 5, 8, getRegister(L.get(2)));
  String value = L.get(3);
  int val = Integer.parseInt(value);
  instruction = embed(instruction, 9, 31, val);
  return instruction;
 }

 // generates a setvalue machine instruction
 private static int assembleLoadAddress(int opcode, List<String> L, Map<String,Integer> labels) {
  int instruction = 0;
  instruction = embed(instruction, 0, 4, opcode);
  instruction = embed(instruction, 5, 8, getRegister(L.get(2)));
  String value = L.get(3);
  int addr = labels.get(value);
  instruction = embed(instruction, 9, 31, addr);
  return instruction;
 }

 private static int assembleTwoRegisterOp(int opcode, List<String> L) {  
  int instruction = 0;
  instruction = embed(instruction, 0, 4,  opcode);
  instruction = embed(instruction, 5, 8,  getRegister(L.get(2)));
  instruction = embed(instruction, 9, 13, getRegister(L.get(3)));
  return instruction;
 }
 
 private static int assembleOneRegisterOp(int opcode, List<String> L) {  
  int instruction = 0;
  instruction = embed(instruction, 0, 4,  opcode);
  instruction = embed(instruction, 5, 8,  getRegister(L.get(2)));
  return instruction;
 }
 
 private static int assembleZeroRegisterOp(int opcode) {
  int instruction = 0;
  instruction = embed(instruction, 0, 4, opcode);
  return instruction;
 }

 private static int getRegister(String reg) {
  reg = reg.substring(1, reg.length()); // remove the "r" prefix
  return Integer.parseInt(reg);
 }

 // Embed a value within a range of bits of an int.
 private static int embed(int n, int low, int high, int val) {
  int pos = 0;
  for (int i=low; i<=high; i++) {
   int bit = getBit(val, pos);
   n = setBit(n, i, bit);
   pos++;
  }
  return n;
 }

 private static int getBit(int n, int pos) {
  return (n >>> pos) % 2;
 }
 
 private static int setBit(int n, int pos, int val) {
  if (pos == 31) 
   return setSignBit(n, val);
  int mask = (1 << pos);
  if (val == 0)
   return n & ~mask;
  else
   return n | mask;
 }
 
 private static int setSignBit(int n, int val) {
  if ((n>=0 && val==0) || (n<0 && val==1))
   return n;
  if (n>=0) 
   return n + Integer.MIN_VALUE;
  else
   return n - Integer.MIN_VALUE;
 }
 
 static class ProgramInfo {
  public Map<String, Integer> symbols = new HashMap<>();
  public List<List<String>> codelines = new ArrayList<>();
 }
}
