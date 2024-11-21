/*
    Implementation of a 16-bit Mini-AES Algorithm
    ------------------------------------------------
    Author: [Benhatta mokhttar]
    Date: [20-10.2024]

    OVERVIEW:
    This algorithm is a simplified, 16-bit version of the AES (Advanced Encryption Standard) algorithm. 
    Designed to mimic the steps of the 128-bit AES, this miniature version follows the same core stages 
    (SubBytes, ShiftRows, MixColumns, and AddRoundKey) but with a smaller S-Box table and key size 
    for easier understanding and testing. AES is a symmetric encryption algorithm widely used in various 
    security applications and protocols (e.g., TLS, VPNs, file encryption). 

    OBJECTIVE:
    The goal is to implement a basic 16-bit version of AES without any external help from the web or AI 
    to fully understand the principles of the AES cipher. Once a working version is complete, the focus 
    will shift to optimizing and analyzing the algorithm’s complexity for improved performance.

    AES KEY VARIANTS:
    Standard AES comes in three key sizes: 
      - AES-128 (128-bit key)
      - AES-192 (192-bit key)
      - AES-256 (256-bit key)
    Each version undergoes multiple transformation rounds depending on the key size, offering a balance 
    of security and performance suited for different applications. This 16-bit Mini-AES implementation 
    provides a simplified learning approach to grasp the algorithm’s structure before progressing to full-scale AES.

    NOTE:
    This Mini-AES lacks certain security elements of the full AES and is not suitable for real-world 
    security applications. Its purpose is purely educational to demonstrate the steps of a block cipher.

    STRUCTURE:
    - Initial Key Expansion: Generates subkeys used in each round.
    - SubBytes Transformation: Uses an S-Box substitution to enhance non-linearity.
    - ShiftRows Transformation: Row shifting to provide diffusion.
    - MixColumns Transformation: Matrix multiplication in GF(2^4) (for educational simplification).
    - AddRoundKey: XOR operation between the current state and subkeys.
    
    FUTURE WORK:
    - Improve computational complexity by optimizing loops and data handling.
    - Extend to larger block sizes (e.g., 32 or 64 bits) before moving to 128-bit AES.
    - Evaluate and implement side-channel resistance strategies.
*/

import java.util.HashMap;
import java.util.Map;

public class AES {
    // TODO make it work column column now it working line by line (just do j insted
    // of i)

    // mini aes only two rounds,key is a 16 bits length;
    int roundes = 2;
    // TODO add firs round key
    // a hash map to store the sbox table
    HashMap<String, String> NibbleSubTable = new HashMap<String, String>();
    // hash map have the decimal value with the value into binary
    HashMap<String, Integer> BinaryValue = new HashMap<String, Integer>();

    String[][] s_boxValue = new String[2][2];
    // Plain text matrx
    String[][] plainText = { { "9", "6" }, { "C", "3" } };

    // key matrix
    String[][] Key = { { "C", "F" }, { "3", "0" } };
    // Constant Matrix for mixColumns
    String[][] ConstantMatrix = {
            { "3", "2" },
            { "2", "3" }
    };

    // function to display the s-box matrix
    // DONE
    public void display(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                // System.out.println(matrix[i][j].toBinaryString(matrix[i][j])); //this is for
                // the test of the binary
                System.out.println(matrix[i][j]);
            }
        }
    }

    public void displayBinary(Integer[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.println(matrix[i][j]);
            }
        }
    }

    public int[][] MixColumns(String[][] shiftRowsMatrix, String[][] ConstantMatrix) {
        int[][] resultMatrix = new int[2][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int sum = 0;
                for (int k = 0; k < 2; k++) {
                    int a = Integer.parseInt(shiftRowsMatrix[k][j], 16);
                    int b = Integer.parseInt(ConstantMatrix[i][k], 16);
                    sum ^= multiplyInGF(a, b);
                }
                resultMatrix[i][j] = sum;
            }
        }
        System.out.println("Resulting MixColumns Matrix:");
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = 0; j < resultMatrix[i].length; j++) {
                System.out.printf("%02X ", resultMatrix[i][j]);
            }
            System.out.println();
        }
        return resultMatrix;
    }

    public int multiplyInGF(int a, int b) {
        int result = 0;
        int irreduciblePolynomial = 0b10011;

        while (b > 0) {
            if ((b & 1) != 0) {
                result ^= a;
            }
            a <<= 1;
            if ((a & 0b10000) != 0) {
                a ^= irreduciblePolynomial;
            }
            b >>= 1;
        }
        // format the result into hex
        return result & 0xF;
    }

    // Helper function to convert hex string to polynomial binary representation
    private String hexToPolynomial(String hexValue) {
        // Convert hex to integer
        int decimal = Integer.parseInt(hexValue, 16);
        // Convert integer to binary string and pad to 4 bits
        String binary = Integer.toBinaryString(decimal);
        return String.format("%4s", binary).replace(' ', '0'); // Ensure 4-bit length
    }

    // THe input is the matrix the plain text or the key we are tranforming the
    // values into
    // binary so we can do an XOR
    // DONE
    public Integer[][] toBinary(String[][] plainText) {
        Integer[][] binaryMatrix = new Integer[plainText.length][plainText.length];
        BinaryValue.put("0", 0x0);
        BinaryValue.put("1", 0x1);
        BinaryValue.put("2", 0x2);
        BinaryValue.put("3", 0x3);
        BinaryValue.put("4", 0x4);
        BinaryValue.put("5", 0x5);
        BinaryValue.put("6", 0x6);
        BinaryValue.put("7", 0x7);
        BinaryValue.put("8", 0x8);
        BinaryValue.put("9", 0x9);
        BinaryValue.put("A", 0xA);
        BinaryValue.put("B", 0xB);
        BinaryValue.put("C", 0xC);
        BinaryValue.put("D", 0xD);
        BinaryValue.put("E", 0xE);
        BinaryValue.put("F", 0xF);
        for (Map.Entry<String, Integer> entry : BinaryValue.entrySet()) {
            for (int i = 0; i < plainText.length; i++) {
                for (int j = 0; j < plainText[i].length; j++) {
                    if (plainText[i][j].equals(entry.getKey())) {
                        binaryMatrix[i][j] = BinaryValue.get(plainText[i][j]);
                    }
                }
            }
        }
        return binaryMatrix;
    }

    // this function is going to do the xor operation (addsubkey phase)
    // The inputs are the matrix , the output is gonna be incha'Allah the new matrix
    // to pass it to NibbleSub funciton
    // Done (Phase one of the algorithme )
    public Integer[][] addSubKey(String[][] plainText, String[][] key) {
        Integer[][] plainTextBinary = toBinary(plainText); // Convert plain text to binary integers
        Integer[][] keyBinary = toBinary(key); // Convert key to binary integers

        Integer[][] resultMatrix = new Integer[plainTextBinary.length][plainTextBinary[0].length];

        for (int j = 0; j < plainTextBinary[0].length; j++) {
            for (int i = 0; i < plainTextBinary.length; i++) {
                resultMatrix[i][j] = plainTextBinary[i][j] ^ keyBinary[i][j];
            }
        }
        System.out.println("Result of Add Sub Round Key is:");
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = 0; j < resultMatrix[i].length; j++) {
                System.out.print("" + Integer.toHexString(resultMatrix[i][j]).toUpperCase());
            }
            System.err.println(" ");
        }

        return resultMatrix;
    }

    // The input is the plain text after the addSubkey phase
    // Done(Phase two of the algorithme)
    public String[][] NibbleSub(String[][] value) {
        // S-BOX Table
        NibbleSubTable.put("0", "E");
        NibbleSubTable.put("1", "4");
        NibbleSubTable.put("2", "D");
        NibbleSubTable.put("3", "1");
        NibbleSubTable.put("4", "2");
        NibbleSubTable.put("5", "F");
        NibbleSubTable.put("6", "B");
        NibbleSubTable.put("7", "8");
        NibbleSubTable.put("8", "3");
        NibbleSubTable.put("9", "A");
        NibbleSubTable.put("A", "6");
        NibbleSubTable.put("B", "C");
        NibbleSubTable.put("C", "5");
        NibbleSubTable.put("D", "9");
        NibbleSubTable.put("E", "0");
        NibbleSubTable.put("F", "7");

        String result[][] = new String[2][2];
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[i].length; j++) { // Corrected the loop to match the actual columns
                String current_value = value[i][j];
                // Using equals to compare the current value with the key in the NibbleSubTable
                if (NibbleSubTable.containsKey(current_value.toString())) {
                    result[i][j] = NibbleSubTable.get(current_value);
                }
            }
        }
        // display the reuslt
        System.out.println("Result of NiblleSub is:");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j]);
            }
            System.out.println(" ");
        }
        return result;
    }

    public String[][] INVNibbleSub(String[][] value) {
        // S-BOX Table
        NibbleSubTable.put("E", "0");
        NibbleSubTable.put("4", "1");
        NibbleSubTable.put("D", "2");
        NibbleSubTable.put("1", "3");
        NibbleSubTable.put("2", "4");
        NibbleSubTable.put("F", "5");
        NibbleSubTable.put("B", "6");
        NibbleSubTable.put("8", "7");
        NibbleSubTable.put("3", "8");
        NibbleSubTable.put("A", "9");
        NibbleSubTable.put("6", "A");
        NibbleSubTable.put("C", "B");
        NibbleSubTable.put("5", "C");
        NibbleSubTable.put("9", "D");
        NibbleSubTable.put("0", "E");
        NibbleSubTable.put("7", "F");

        String result[][] = new String[2][2];
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[i].length; j++) { // Corrected the loop to match the actual columns
                String current_value = value[i][j];
                // Using equals to compare the current value with the key in the NibbleSubTable
                if (NibbleSubTable.containsValue(current_value.toString())) {
                    result[i][j] = NibbleSubTable.get(current_value);
                }
            }
        }
        // display the reuslt
        System.out.println("Result of NiblleSub is:");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j]);
            }
            System.out.println(" ");
        }
        return result;
    }

    public String NibbleSubValue(String value) {
        // S-BOX Table
        NibbleSubTable.put("0", "E");
        NibbleSubTable.put("1", "4");
        NibbleSubTable.put("2", "D");
        NibbleSubTable.put("3", "1");
        NibbleSubTable.put("4", "2");
        NibbleSubTable.put("5", "F");
        NibbleSubTable.put("6", "B");
        NibbleSubTable.put("7", "8");
        NibbleSubTable.put("8", "3");
        NibbleSubTable.put("9", "A");
        NibbleSubTable.put("A", "6");
        NibbleSubTable.put("B", "C");
        NibbleSubTable.put("C", "5");
        NibbleSubTable.put("D", "9");
        NibbleSubTable.put("E", "0");
        NibbleSubTable.put("F", "7");
        String current_value = value;
        if (NibbleSubTable.containsKey(current_value)) {
            value = NibbleSubTable.get(current_value);
        }
        System.out.println(value);
        return value;
    }

    // TODO it working but make sure to make it better to work in large exemples
    public String[][] shiftRows(String[][] NibbleSub_matrix) {
        String[][] result = new String[NibbleSub_matrix.length][NibbleSub_matrix[0].length];
        result[0][0] = NibbleSub_matrix[0][0];
        result[0][1] = NibbleSub_matrix[0][1];
        result[1][0] = NibbleSub_matrix[1][1];
        result[1][1] = NibbleSub_matrix[1][0];

        // display the reuslt
        System.out.println("Shit Rows resutl:");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j]);
            }
            System.out.println("");
        }
        return result;
    }

    public String InvNibbleSubValue(String value) {
        // S-BOX Table
        NibbleSubTable.put("0", "E");
        NibbleSubTable.put("1", "4");
        NibbleSubTable.put("2", "D");
        NibbleSubTable.put("3", "1");
        NibbleSubTable.put("4", "2");
        NibbleSubTable.put("5", "F");
        NibbleSubTable.put("6", "B");
        NibbleSubTable.put("7", "8");
        NibbleSubTable.put("8", "3");
        NibbleSubTable.put("9", "A");
        NibbleSubTable.put("A", "6");
        NibbleSubTable.put("B", "C");
        NibbleSubTable.put("C", "5");
        NibbleSubTable.put("D", "9");
        NibbleSubTable.put("E", "0");
        NibbleSubTable.put("F", "7");
        String current_value = value;
        if (NibbleSubTable.containsValue(current_value)) {
            value = NibbleSubTable.get(current_value);
            System.out.println(value);
        }
        System.out.println(value);
        return value;
    }

    public String[] KeyGeneration(String[][] Key) {
        String[] words = new String[12];

        // RCON values
        String reconOne = "1";
        String reconTwo = "2";

        for (int i = 0; i < Key.length; i++) {
            for (int j = 0; j < Key[i].length; j++) {
                words[i * Key[i].length + j] = Key[j][i];
            }
        }
        // First round keys
        String r1 = xorHex(words[0], NibbleSubValue(words[3]));
        System.out.println(r1);
        String r2 = xorHex(r1, reconOne);
        System.out.println(r2);

        words[4] = r2;
        words[5] = xorHex(words[1], words[4]);
        words[6] = xorHex(words[2], words[5]);
        words[7] = xorHex(words[3], words[6]);

        // Second round keys
        String r3 = xorHex(words[4], NibbleSubValue(words[7]));
        System.out.println(r3);
        String r4 = xorHex(r3, reconTwo);
        System.out.println(r4);

        words[8] = r4;
        words[9] = xorHex(words[5], words[8]);
        words[10] = xorHex(words[6], words[9]);
        words[11] = xorHex(words[7], words[10]);

        // Display generated keys
        System.out.println("Generated Keys:");
        for (int i = 0; i < words.length; i++) {
            System.out.println("Word " + i + ": " + words[i]);
        }

        return words;

    }

    // method to cast Integer[][] to Stringp[][]
    public String[][] castToString(Integer[][] matrix) {
        String[][] result = new String[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = Integer.toHexString(matrix[i][j]).toUpperCase();
            }
        }

        return result;
    }

    // method to cast int to Integer[][]
    public Integer[][] castToInteger(int[][] matrix) {
        Integer[][] castResult = new Integer[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                castResult[i][j] = Integer.valueOf(matrix[i][j]);
            }
        }

        return castResult;
    }

    // XOR two hexadecimal strings
    private String xorHex(String hex1, String hex2) {
        // Convert both hex strings to integers
        int num1 = Integer.parseInt(hex1, 16);
        int num2 = Integer.parseInt(hex2, 16);

        // Perform XOR operation
        int result = num1 ^ num2;

        // Convert result back to hex and return as string
        return Integer.toHexString(result).toUpperCase();
    }

    // Dummy NibbleSubValue function for substitution (replace with actual
    // implementation)
    private String nibbleSubValue(String hex) {
        // For example, perform some nibble substitution here
        // Replace with actual substitution logic
        return Integer.toHexString((Integer.parseInt(hex, 16) + 1) % 16).toUpperCase();
    }

    public void ExtractRoundKeys(String[] words) {
        String[][] keys = new String[2][2];

        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys[i].length; j++) {

            }
        }

    }

    // Method to convert Matrix to Array
    public Integer[][] Encryption() {

        int rounds = 0;
        String[] geenratedWords = KeyGeneration(Key);

        // round zero
        // Add sub key phase
        System.out.println("ROUND ZERO:");
        Integer[][] addSubResult = addSubKey(plainText, Key);
        rounds++;
        // ROUND ONE
        String[][] keynOne = { { geenratedWords[4], geenratedWords[6] }, { geenratedWords[5], geenratedWords[7] } };
        // Nibble substitution
        String[][] NibbleSubResult = NibbleSub(castToString(addSubResult));
        // shift Rows
        String[][] shiftRowResult = shiftRows(NibbleSubResult);
        // mix Columns Result
        int[][] MixColumnsResult = MixColumns(shiftRowResult, ConstantMatrix);
        addSubResult = addSubKey(castToString(castToInteger(MixColumnsResult)), keynOne);
        rounds++;
        // ROUND TWO
        String[][] KeyTwo = { { geenratedWords[8], geenratedWords[10] }, { geenratedWords[9], geenratedWords[11] } };
        NibbleSubResult = NibbleSub(castToString(addSubResult));
        shiftRowResult = shiftRows(NibbleSubResult);
        addSubResult = addSubKey(shiftRowResult, KeyTwo);

        Integer[][] CipherText = addSubResult;
        return CipherText;

    }

    public Integer[][] Decryption(Integer[][] cipherText) {

        int rounds = 0;
        String[] generatedWords = KeyGeneration(Key);
        String[][] keynOne = { { generatedWords[4], generatedWords[6] }, { generatedWords[5], generatedWords[7] } };

        String[][] KeyTwo = { { generatedWords[8], generatedWords[10] }, { generatedWords[9], generatedWords[11] } };

        Integer[][] addSubResult = addSubKey(castToString(cipherText), KeyTwo);
        rounds++;

        String[][] invShiftRows = shiftRows(castToString(addSubResult));

        String[][] invNiblleSub = INVNibbleSub(invShiftRows);

        addSubResult = addSubKey(invNiblleSub, keynOne);

        int[][] invMixColumns = MixColumns(castToString(addSubResult), ConstantMatrix);
        invShiftRows = shiftRows(castToString(castToInteger(invMixColumns)));
        invNiblleSub = INVNibbleSub(invShiftRows);
        addSubResult = addSubKey(invNiblleSub, Key);
        rounds++;

        Integer[][] decryptedText = addSubResult;
        return decryptedText;
    }

    public static void main(String[] args) {
        AES miniAES = new AES();
        Integer[][] cypherText = miniAES.Encryption();
        System.out.println("Decription...... ");
        Integer[][] PlainText = miniAES.Decryption(cypherText);

    }

}
