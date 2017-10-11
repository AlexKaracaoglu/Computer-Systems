//package simplecomputer;
import java.io.*;
import java.util.Scanner;

public class Emulator {
 // Memory.
 public static final int RAMSIZE = 1000;
 static int[] memory  = new int[RAMSIZE];

 // The three special-purpose registers.
 static int PC = 0;         // the program counter
 static int IR;             // the instruction register
 static int PSR;            // the program status register

 // The 16 general purpose registers.  
 static int[] register = new int[16]; 

 // The I/O devices.
 static Reader      keyboard = new InputStreamReader(System.in);
 static PrintStream printer = System.out;

 // A boolean variable to enable debugging output
 static boolean debug;

 public static void main(String[] args) {
  register[15] = RAMSIZE*4;  // register[15] is the stack pointer
  System.out.print("Enter executable file name: ");
  Scanner scanner = new Scanner(System.in);
  String filename = scanner.next();
  try (Reader rdr = new FileReader(filename)) {
   loadProgram(rdr);
   System.out.println("Program Loaded");
  }
  catch (IOException ex) {
   System.out.println("File " + filename + " not found.");
   System.exit(0);
  }

  System.out.print("Debug? (y/n): ");
  debug = scanner.next().startsWith("y") ? true : false;

  // The fetch-execute cycle
  while (PC >= 0) {
   IR = memory[PC];
   executeInstruction();
   PC++;
  }
  System.out.println("\nProgram Terminated");
  scanner.close();
 }

 private static void loadProgram(Reader rdr) {
  try (Scanner scanner = new Scanner(rdr)) {
   int wordnum = 0;
   while (scanner.hasNextInt()) {
    int instruction = scanner.nextInt();
    memory[wordnum] = instruction; 
    wordnum++;
   }
  }
 }

 private static void executeInstruction() {
  int opcode = extract(IR, 0, 4, false);
  if      (opcode == 0)  executeSystem();
  else if (opcode == 1)  executeCompare();
  else if (opcode == 2)  executeJump();
  else if (opcode == 3)  executeJumpEq();
  else if (opcode == 4)  executeJumpNe();
  else if (opcode == 5)  executeJumpLt();
  else if (opcode == 6)  executeJumpLe();
  else if (opcode == 7)  executeJumpGt();
  else if (opcode == 8)  executeJumpGe();
  else if (opcode == 9)  executeSetValue();
  else if (opcode == 10) executeAdd();
  else if (opcode == 11) executeSubtract();
  else if (opcode == 12) executeMultiply();
  else if (opcode == 13) executeDivide();
  else if (opcode == 14) executeMod();   
  else if (opcode == 15) executeLoad();
  else if (opcode == 16) executeStore();
  else if (opcode == 17) executeJumpSubroutine();
  else if (opcode == 18) executeReturn();
  else if (opcode == 19) executePushRegister();
  else if (opcode == 20) executePopRegister();
  else if (opcode == 21) executeRestoreRegister();
  else if (opcode == 22) executeLoadSignedByte();
  else if (opcode == 23) executeLoadUnsignedByte();
  else if (opcode == 24) executeStoreByte();
  else
   System.out.println("invalid op code: " + opcode);
 }

 private static void executeSystem() {
  int callnum = extract(IR, 5, 8, false);
  if (callnum == 0)
   systemExit();
  else if (callnum == 1)
   systemRead();
  else if (callnum == 2)
   systemWrite();
 }

 private static void systemExit() {
  if (debug) System.out.println(PC + ": " + "system exit");
  PC = -2;
 }

 private static void systemRead() {
  int rx = extract(IR, 9, 12, false);
  if (debug) System.out.println(PC + ": " + "system read r" + rx);
  try {
   register[rx] = keyboard.read();
  }
  catch (IOException ex) {}
 }

 private static void systemWrite() {
  int rx = extract(IR, 9, 12, false);
  if (debug) System.out.println(PC + ": " + "system write r" + rx);
  printer.print((char)register[rx]);
//  printer.println(register[rx]); -- if you want the value printed as an int
 }

 private static void executeCompare() {
  int rx = extract(IR, 5,  8, false);
  int ry = extract(IR, 9, 12, false);
  if(debug) System.out.println(PC + ": " + "compare r" + rx + " r" + ry);
  if (register[rx] == register[ry])
   PSR = 0;
  else if (register[rx] < register[ry])
   PSR = -1;
  else
   PSR = 1;
 }

 private static void executeJump() {
  int offset = extract(IR, 5, 31, true);  // a signed value
  if (debug) System.out.println(PC + ": " + "jump " + offset);
  PC += offset;
  PC --;  // decrement PC to cancel out upcoming increment
 }

 private static void executeJumpEq() {
  if (debug) System.out.println(PC + ": " + "jumpEq; PSR=" + PSR);
  if (PSR == 0)
   executeJump();
 }

 private static void executeJumpNe() {
  if (debug) System.out.println(PC + ": " + "jumpNe; PSR=" + PSR);
  if (PSR != 0)
   executeJump();
 }

 private static void executeJumpLt() {
  if (debug) System.out.println(PC + ": " + "jumpLt; PSR=" + PSR);
  if (PSR < 0)
   executeJump();
 }

 private static void executeJumpLe() {
  if (debug) System.out.println(PC + ": " + "jumpLe; PSR=" + PSR);
  if (PSR <= 0)
   executeJump();
 }

 private static void executeJumpGt() {
  if (debug) System.out.println(PC + ": " + "jumpGt; PSR=" + PSR);
  if (PSR > 0)
   executeJump();
 }

 private static void executeJumpGe() {
  if (debug) System.out.println(PC + ": " + "jumpGe; PSR=" + PSR);
  if (PSR >= 0)
   executeJump();
 }

 private static void executeSetValue() {
  int rx   = extract(IR, 5,  8, false);
  int val  = extract(IR, 9, 31, true);  // a signed value
  register[rx] = val;
  if (debug) System.out.println(PC + ": " + "setvalue r" + rx + " " + val);
 }

 private static void executeAdd() {
  int rx = extract(IR,  5,  8, false);
  int ry = extract(IR,  9, 12, false);
  int rz = extract(IR, 13, 16, false);
  register[rx] = register[ry] + register[rz];
  if (debug) System.out.println(PC + ": " + "add r" + rx + " r" + ry + " r" + rz);
 }

 private static void executeSubtract() {
  int rx = extract(IR,  5,  8, false);
  int ry = extract(IR,  9, 12, false);
  int rz = extract(IR, 13, 16, false);
  register[rx] = register[ry] - register[rz];
  if (debug) System.out.println(PC + ": " + "subtract r" + rx + " r" + ry + " r" + rz);
 }

 private static void executeMultiply() {
  int rx = extract(IR,  5,  8, false);
  int ry = extract(IR,  9, 12, false);
  int rz = extract(IR, 13, 16, false);
  register[rx] = register[ry] * register[rz];
  if (debug) System.out.println(PC + ": " + "multiply r" + rx + " r" + ry + " r" + rz);
 }

 private static void executeDivide() {
  int rx = extract(IR,  5,  8, false);
  int ry = extract(IR,  9, 12, false);
  int rz = extract(IR, 13, 16, false);
  register[rx] = register[ry] / register[rz];
  if (debug) System.out.println(PC + ": " + "divide r" + rx + " r" + ry + " r" + rz);
 }

 private static void executeMod() {
  int rx = extract(IR,  5,  8, false);
  int ry = extract(IR,  9, 12, false);
  int rz = extract(IR, 13, 16, false);
  register[rx] = register[ry] % register[rz];
  if (debug) System.out.println(PC + ": " + "mod r" + rx + " r" + ry + " r" + rz);
 }

 private static void executeLoad() {  
  int rx = extract(IR, 5,  8, false);
  int ry = extract(IR, 9, 12, false);
  if (debug) System.out.println(PC + ": " + "load r" + rx + " r" + ry);
  int byteaddr = register[ry];
  int wordnum = byteaddr / 4;
  register[rx] = memory[wordnum];
 }

 private static void executeStore() {  
  int rx = extract(IR, 5,  8, false);
  int ry = extract(IR, 9, 12, false);
  if (debug) System.out.println(PC + ": " + "store r" + rx + " r" + ry);
  int val  = register[rx];
  int byteaddr = register[ry];
  int wordnum = byteaddr / 4;
  memory[wordnum] = val;
 }

 private static void executeJumpSubroutine() {
  if (debug) System.out.println(PC + ": " + "jumpsub; PC = " + PC);
  register[15] -= 4;             // push the current PC 
  memory[register[15] / 4] = PC; // value on the stack
  executeJump();
 }

 private static void executeReturn() {
  PC = memory[register[15] / 4]; // restore the saved PC 
  register[15] += 4;             // value from the stack
  if (debug) System.out.println(PC + ": " + "return; PC = " + PC);
 }
 
 private static void executePushRegister() {
  int rx = extract(IR, 5,  8, false);
  if (debug) System.out.println(PC + ": " + "pushreg r" + rx);
  register[15] -= 4;
  memory[register[15]/4] = register[rx];
 }
 
 private static void executePopRegister() {
  if (debug) System.out.println(PC + ": " + "popreg");
  register[15] += 4;
 }

 private static void executeRestoreRegister() {
  int rx = extract(IR, 5,  8, false);
  if (debug) System.out.println(PC + ": " + "restore r" + rx);
  register[rx] = memory[register[15]/4];
  register[15] += 4;
 }
 private static void executeStoreByte() {  
  int rx = extract(IR, 5,  8, false);
  int ry = extract(IR, 9, 12, false);
  int val  = extract(register[rx], 0, 7, false); // grab the first byte
  if (debug) System.out.println(PC + ": " + "storebyte r" + rx + " r" + ry);
  int byteaddr = register[ry];
  int wordnum = byteaddr / 4;
  int bytenum = byteaddr % 4;
  int lowbits = bytenum * 8;
  int highbits = lowbits + 7;
  memory[wordnum] = embed(memory[wordnum], lowbits, highbits, val);
 }

 private static void executeLoadUnsignedByte() {  
  int rx = extract(IR, 5,  8, false);
  int ry = extract(IR, 9, 12, false);
  if (debug) System.out.println(PC + ": " + "loadubyte r" + rx + " r" + ry);
  int byteaddr = register[ry];
  int wordnum = byteaddr / 4;
  int bytenum = byteaddr % 4;
  int lowbits = bytenum * 8;
  int highbits = lowbits + 7;
  register[rx] = extract(memory[wordnum], lowbits, highbits, false);
 }

 private static void executeLoadSignedByte() {  
  int rx = extract(IR, 5,  8, false);
  int ry = extract(IR, 9, 12, false);
  if (debug) System.out.println(PC + ": " + "loadsbyte r" + rx + " r" + ry);
  int byteaddr = register[ry];
  int wordnum = byteaddr / 4;
  int bytenum = byteaddr % 4;
  int lowbits = bytenum * 8;
  int highbits = lowbits + 7;
  register[rx] = extract(memory[wordnum], lowbits, highbits, true);  // a signed value
 }

 // Extract a range of bits from an int
 // and create a new int from them.
 // The variable isSigned specifies whether the high bit
 // should be treated as a sign bit.
 private static int extract(int n, int low, int high, boolean isSigned) {
  int result = (isSigned && getBit(n, high) == 1) ? -1 : 0;
  int pos = 0;
  for (int i=low; i<=high; i++) {
   int bit = getBit(n, i);
   result = setBit(result, pos, bit);
   pos++;
  }
  return result;
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
  return (n >>> pos) % 2; // n's sign bit also gets shifted
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
}
