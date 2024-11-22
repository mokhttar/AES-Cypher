
// BENHATTA MOKHTTAR  MINI-AES IMPLEMENTATION  MASTER CYBER SECURITY 

//WORK DONE :MINI AES 16 BITS.
//           -Completed (NibbleSub,ShiftRows,AddSubKey,MixColumns)
//           -Completed Generate randomly a secret key of 16-bits (4 numbers between 0 and 15)
//           -Completed Compute the two round keys
//           -The code takes a plain text and Devide it into blocks of  16 bits
//           -Completed InvSubbbox
//           -Completed Encryption
//           -Completed Decryption
//           -The exempple that in the code is:Consider the plaintext “9C639C62”, encrypt it with the secret key “C3F0”
//           -Completed:Generate randomly an initialization vector (IV) of 16-bits.   
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AES {

    // mini aes only two rounds,key is a 16 bits length;
    int roundes = 2;
    // TODO add firs round key
    // a hash map to store the sbox table
    HashMap<String, String> NibbleSubTable = new HashMap<String, String>();
    // hash map have the decimal value with the value into binary
    HashMap<String, Integer> BinaryValue = new HashMap<String, Integer>();
    String[][] s_boxValue = new String[2][2];
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

    public static int[] generateSecretKey() {
        Random random = new Random();
        int[] secretKey = new int[4];

        for (int i = 0; i < 4; i++) {
            secretKey[i] = random.nextInt(16);
        }

        return secretKey;
    }

    // function to devise plain text into blocks of 16-bits
    public String[] DevideIntoBlock(String PlainText) {
        // Input: Plain Text String
        // Output: Matrices, each one 16 bits (4 characters per block)
        int blockCount = (int) Math.ceil((double) PlainText.length() / 4);
        char[] blocks = PlainText.toCharArray();

        String[] blockContent = new String[blockCount];
        for (int i = 0; i < blockContent.length; i++) {
            blockContent[i] = "";
        }

        for (int i = 0; i < blocks.length; i++) {
            int currentBlock = i / 4;
            blockContent[currentBlock] += blocks[i];
        }
        for (int i = 0; i < blockContent.length; i++) {
            System.out.println("Block " + (i + 1) + ": " + blockContent[i]);
        }
        return blockContent;
    }

    // function to transform Block to matrix's
    public String[][][] createMatricesForBlocks(String plainText) {
        int blockSize = 4; // Each block contains 4 characters
        int matrixCount = (int) Math.ceil((double) plainText.length() / blockSize);
        String[][][] matrices = new String[matrixCount][2][2];

        char[] chars = plainText.toCharArray();
        int index = 0;

        for (int i = 0; i < matrixCount; i++) {
            for (int col = 0; col < 2; col++) {
                for (int row = 0; row < 2; row++) {
                    if (index < chars.length) {
                        matrices[i][row][col] = String.valueOf(chars[index]);
                    } else {
                        matrices[i][row][col] = "0";
                    }
                    index++;
                }
            }
        }

        return matrices;
    }

    public void displayMatrices(String[][][] matrices) {
        for (int i = 0; i < matrices.length; i++) {
            System.out.println("Block " + (i + 1) + ":");
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    System.out.print(matrices[i][row][col] + " ");
                }
                System.out.println();
            }
            // System.out.println();
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

        int decimal = Integer.parseInt(hexValue, 16);

        String binary = Integer.toBinaryString(decimal);
        return String.format("%4s", binary).replace(' ', '0');
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
        Integer[][] plainTextBinary = toBinary(plainText);
        Integer[][] keyBinary = toBinary(key);

        Integer[][] resultMatrix = new Integer[plainTextBinary.length][plainTextBinary[0].length];

        for (int j = 0; j < plainTextBinary[0].length; j++) {
            for (int i = 0; i < plainTextBinary.length; i++) {
                resultMatrix[i][j] = plainTextBinary[i][j] ^ keyBinary[i][j];
            }
        }

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
            for (int j = 0; j < value[i].length; j++) {
                String current_value = value[i][j];

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
            for (int j = 0; j < value[i].length; j++) {
                String current_value = value[i][j];

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
        String r2 = xorHex(r1, reconOne);

        words[4] = r2;
        words[5] = xorHex(words[1], words[4]);
        words[6] = xorHex(words[2], words[5]);
        words[7] = xorHex(words[3], words[6]);

        // Second round keys
        String r3 = xorHex(words[4], NibbleSubValue(words[7]));
        String r4 = xorHex(r3, reconTwo);

        words[8] = r4;
        words[9] = xorHex(words[5], words[8]);
        words[10] = xorHex(words[6], words[9]);
        words[11] = xorHex(words[7], words[10]);

        return words;

    }

    public String[][] castToString(Integer[][] matrix) {
        String[][] result = new String[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = Integer.toHexString(matrix[i][j]).toUpperCase();
            }
        }
        return result;
    }

    public Integer[][] castToInteger(int[][] matrix) {
        Integer[][] castResult = new Integer[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                castResult[i][j] = Integer.valueOf(matrix[i][j]);
            }
        }

        return castResult;
    }

    private String xorHex(String hex1, String hex2) {

        int num1 = Integer.parseInt(hex1, 16);
        int num2 = Integer.parseInt(hex2, 16);

        int result = num1 ^ num2;

        return Integer.toHexString(result).toUpperCase();
    }

    public void ExtractRoundKeys(String[] words) {
        String[][] keys = new String[2][2];

        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys[i].length; j++) {

            }
        }

    }

    // Method to convert Matrix to Array
    public Integer[][] Encryption(String[][] PlainText) {

        int rounds = 0;
        String[] geenratedWords = KeyGeneration(Key);

        // round zero
        // Add sub key phase
        System.out.println("ROUND ZERO:----------------------------------------->");
        System.out.println("Result of Add Sub Round Key (ROUND ZERO) is:");
        Integer[][] addSubResult = addSubKey(PlainText, Key);
        rounds++;
        System.out.println("ROUND ONE:----------------------------------------->");
        // ROUND ONE
        String[][] keynOne = { { geenratedWords[4], geenratedWords[6] }, { geenratedWords[5], geenratedWords[7] } };
        // Nibble substitution
        String[][] NibbleSubResult = NibbleSub(castToString(addSubResult));
        // shift Rows
        String[][] shiftRowResult = shiftRows(NibbleSubResult);
        // mix Columns Result
        int[][] MixColumnsResult = MixColumns(shiftRowResult, ConstantMatrix);
        System.out.println("Result of Add Sub Round Key is(BEFORE ROUND TWO):");
        addSubResult = addSubKey(castToString(castToInteger(MixColumnsResult)), keynOne);
        rounds++;
        // ROUND TWO
        System.out.println("ROUND TWO----------------------------------------->");
        String[][] KeyTwo = { { geenratedWords[8], geenratedWords[10] }, { geenratedWords[9], geenratedWords[11] } };
        NibbleSubResult = NibbleSub(castToString(addSubResult));
        shiftRowResult = shiftRows(NibbleSubResult);
        System.out.println("The Cipher text is :(Last Round)");
        addSubResult = addSubKey(shiftRowResult, KeyTwo);

        Integer[][] CipherText = addSubResult;
        return CipherText;

    }

    public String generateRandomIV() {
        Random random = new Random();
        int iv = random.nextInt(0xFFFF + 1);
        return String.format("%04X", iv);
    }

    public Integer[][] Decryption(Integer[][] cipherText) {

        int rounds = 0;
        String[] generatedWords = KeyGeneration(Key);
        String[][] keynOne = { { generatedWords[4], generatedWords[6] }, { generatedWords[5], generatedWords[7] } };

        String[][] KeyTwo = { { generatedWords[8], generatedWords[10] }, { generatedWords[9], generatedWords[11] } };
        System.out.println("ROUND ZERO OF DECRIPTION:----------------------------------------->");
        Integer[][] addSubResult = addSubKey(castToString(cipherText), KeyTwo);
        rounds++;
        System.out.println("ROUND ONE OF DECRIPTION:----------------------------------------->");
        String[][] invShiftRows = shiftRows(castToString(addSubResult));

        String[][] invNiblleSub = INVNibbleSub(invShiftRows);

        addSubResult = addSubKey(invNiblleSub, keynOne);
        System.out.println("ROUND TWO OF DECRIPTION:----------------------------------------->");
        int[][] invMixColumns = MixColumns(castToString(addSubResult), ConstantMatrix);
        invShiftRows = shiftRows(castToString(castToInteger(invMixColumns)));
        invNiblleSub = INVNibbleSub(invShiftRows);
        System.out.println("PLAIN TEXT For The  Round  :----------------------------------------->");

        addSubResult = addSubKey(invNiblleSub, Key);
        rounds++;

        Integer[][] decryptedText = addSubResult;
        return decryptedText;
    }

    public void displayMatrix(Integer[][] matrix) {
        for (Integer[] row : matrix) {
            for (Integer value : row) {
                System.out.print(String.format("%02X ", value));
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {

        AES miniAES = new AES();
        String text = "9C63C62";
        String cypherResult = "";
        // KEY IS String[][] Key = { { "C", "F" }, { "3", "0" } }; IT A GLOBAL VARIAVLE
        // LOOK UP IN THE FIRST LINES OF THE CODE
        String[][][] matrices = miniAES.createMatricesForBlocks(text);
        miniAES.displayMatrices(matrices);

        for (int matrixIndex = 0; matrixIndex < matrices.length; matrixIndex++) {
            System.out.println(
                    "---------------------------------------------------------------------Crypting  Block Number  "
                            + matrixIndex
                            + " --------------------------------------------------------------------------");
            System.out.println();
            String[][] currentMatrix = matrices[matrixIndex];
            // Encrypt the Integer matrix
            Integer[][] cypherText = miniAES.Encryption(currentMatrix);
            // Display the encrypted block
            System.out.println("Encrypted Block " + (matrixIndex + 1) + ":");
            miniAES.displayMatrix(cypherText);
            System.out.println(
                    "---------------------------------------------------------------------Decrypting  Block Number  "
                            + matrixIndex
                            + " --------------------------------------------------------------------------");
            Integer[][] PlainText = miniAES.Decryption(cypherText);
            System.out.println("Decrypted Block " + (matrixIndex + 1) + ":");
            miniAES.displayMatrix(PlainText);
        }

    }
}
