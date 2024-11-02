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
    String[][] plainText = { { "A", "9" }, { "2", "4" } };
    // key matrix
    String[][] Key = { { "B", "2" }, { "C", "2" } };

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

    // only for debugin to see the result of to binary funciton
    public void displayBinary(Integer[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.println(matrix[i][j]);
            }
        }
    }

    public void toHexDisplay(Integer[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(Integer.toHexString(matrix[i][j]));
            }
        }
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
        /*
         * {A,9} {B,2} A9B2 XOR BC22 ==> 10100010010010100
         * xor
         * 1011110000100010
         * {2,4} {C,2}
         */
        for (int j = 0; j < plainTextBinary[0].length; j++) {
            for (int i = 0; i < plainTextBinary.length; i++) {
                resultMatrix[i][j] = plainTextBinary[i][j] ^ keyBinary[i][j];
            }
        }

        System.out.println("Result of AddRoundKey (XOR operation):");
        displayBinary(resultMatrix);
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
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[i].length; j++) {
                String current_value = value[i][j];
                if (NibbleSubTable.containsKey(current_value)) {
                    value[i][j] = NibbleSubTable.get(current_value);
                }
            }
        }
        return value;
    }

    // TODO it working but make sure to make it better to work in large exemples
    public String[][] shiftRows(String[][] NibbleSub_matrix) {
        String[][] result = new String[NibbleSub_matrix.length][NibbleSub_matrix[0].length];
        // we know that the length of the matrix is a 2*2
        // swap the second line
        result[0][0] = NibbleSub_matrix[0][0];
        result[0][1] = NibbleSub_matrix[0][1];
        result[1][0] = NibbleSub_matrix[1][1];
        result[1][1] = NibbleSub_matrix[1][0];
        display(result);
        return result;
    }

    public static void main(String[] args) {
        AES aes = new AES();
        String[][] plainText = { { "A", "9" }, { "2", "4" } };
        String[][] Key = { { "B", "2" }, { "C", "2" } };
        // Integer[][] test = aes.toBinary(plainText);
        // aes.toHexDisplay(aes.addSubKey(plainText, Key));
        aes.shiftRows(plainText);
    }
}

// Result of xor operation column by column
// 1010 1001 0010 0100
// 1011 0010 1100 0010
// -------------------------
// 0001 1011 1110 0110