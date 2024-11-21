public class Main {
     public static void main(String[] args) {
          AES aes = new AES();

          // Display the initial plaintext and key
          System.out.println("Initial Plaintext:");
          aes.display(aes.plainText);

          System.out.println("\nInitial Key:");
          aes.display(aes.Key);

          // Step 1: AddRoundKey
          Integer[][] roundKeyMatrix = aes.addSubKey(aes.plainText, aes.Key);

          // Step 2: Nibble Substitution
          String[][] nibbleSubMatrix = aes.NibbleSub(aes.plainText);
          System.out.println("\nAfter NibbleSub Transformation:");
          aes.display(nibbleSubMatrix);

          // Step 3: Shift Rows
          String[][] shiftRowsMatrix = aes.shiftRows(nibbleSubMatrix);
          System.out.println("\nAfter ShiftRows Transformation:");
          aes.display(shiftRowsMatrix);

          // Step 4: Mix Columns
          Integer[][] binaryShiftRowsMatrix = aes.toBinary(shiftRowsMatrix);
          Integer[][] mixColumnsMatrix = aes.mixColumns(binaryShiftRowsMatrix);

          System.out.println("\nAfter MixColumns Transformation:");
          aes.displayBinary(mixColumnsMatrix);


     }
}
