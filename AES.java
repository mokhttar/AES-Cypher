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

    // THe input is the matrix the plain text or the key we are tranforming the
    // values into
    // binary so we can do an XOR
    public Integer[][] toBinary(String[][] plainText) {
        Integer[][] binaryMatrix = new Integer[plainText.length][plainText.length];
        BinaryValue.put("0", 0b0000);
        BinaryValue.put("1", 0b0001);
        BinaryValue.put("2", 0b0010);
        BinaryValue.put("3", 0b0011);
        BinaryValue.put("4", 0b0100);
        BinaryValue.put("5", 0b0101);
        BinaryValue.put("6", 0b0110);
        BinaryValue.put("7", 0b0111);
        BinaryValue.put("8", 0b1000);
        BinaryValue.put("9", 0b1001);
        BinaryValue.put("A", 0b1010);
        BinaryValue.put("B", 0b1011);
        BinaryValue.put("C", 0b1100);
        BinaryValue.put("D", 0b1101);
        BinaryValue.put("E", 0b1110);
        BinaryValue.put("F", 0b1111);
        for (Map.Entry<String, Integer> entry : BinaryValue.entrySet()) {
            Integer binaryValue = entry.getValue();
            for (int i = 0; i < plainText.length; i++) {
                for (int j = 0; j < plainText[i].length; j++) {
                    if (plainText[i][j].toString() == entry.getKey()) {
                        binaryMatrix[i][j] = binaryValue;
                    }

                }
            }
        }
        return binaryMatrix;
    }

    // this function is going to do the xor operation (addsubkey phase)
    // The inputs are the matrix , the output is gonna be incha'Allah the new matrix
    // to pass it to NibbleSub funciton

    public void addSubKey(String[][] plainText, String[][] key) {

        // Note the key is a 16bit.
        int matrix_length = plainText.length;
        Integer[][] binaryMatrix = new Integer[matrix_length][matrix_length];
        Integer[][] binaryKey = new Integer[key.length][key.length];// this size it dose not matter to me because it a
                                                                    // 2*2 matrix
        Integer[][] result = new Integer[plainText.length][plainText.length];
        binaryMatrix = toBinary(plainText);
        binaryKey = toBinary(key);
        for (int i = 0; i < plainText.length; i++) {
            for (int j = 0; j < plainText[i].length; j++) {
                result[i][j] = binaryMatrix[i][j] ^ binaryKey[i][j]; // check if the XOR symbole is like that
                // display to see the result
                System.out.print(result[i][j]);
            }
        }
    }

    // DONE
    // The input is the plain text after the addSubkey phase
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
        for (Map.Entry<String, String> entry : NibbleSubTable.entrySet()) {
            for (int i = 0; i < value.length; i++) {
                for (int j = 0; j < value[i].length; j++) {
                    if (value[i][j] == entry.getKey()) {
                        String new_value = entry.getValue();
                        value[i][j] = new_value;
                    }
                }
            }
        }
        return value;
    }

    // function to display the s-box matrix
    // DONE
    public void display(Integer[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.println(matrix[i][j].toBinaryString(matrix[i][j]));
            }
        }
    }

    public static void main(String[] args) {
        AES aes = new AES();
        String[][] plainText = { { "A", "9" }, { "2", "4" } };
        // test Tobinaryfuntion(this function is transfomring the input of the plain
        // matrix or any matrix to it binary values
        Integer[][] test = aes.toBinary(plainText);
        aes.display(test);

        // THIS IS THE NEW MATRIX AFTER THE NIBBLE SUB
        // String[][] BubbleMatrix = aes.NibbleSub(plainText);
    }
}
