//package org.example;
//
//import static org.example.CryptoMix.generateHash;
//
//public class teste {
//    @Override
//    public void onMessage(String message) {
//        System.out.println(message);
//
//        // Extract the method and input from the message
//        String method = extractMethod(message);
//        String input = extractInput(message);
//        String encodedInput = extractEncodedInput(message);
//
//        if (method != null && input != null) {
//            switch (method.toLowerCase()) {
//                case "md5":
//                    String md5Hash = generateHash("MD5", input);
//                    send(md5Hash);
//                    break;
//                case "sha1":
//                    String sha1Hash = generateHash("SHA-1", input);
//                    send(sha1Hash);
//                    break;
//                default:
//                    if (encodedInput != null) {
//                        String decodedMessage;
//                        switch (method.toLowerCase()) {
//                            case "base64":
//                                decodedMessage = decodeBase64(encodedInput);
//                                break;
//                            case "binario":
//                                decodedMessage = convertFromBinary(encodedInput);
//                                break;
//                            case "hex":
//                                decodedMessage = convertFromHex(encodedInput);
//                                break;
//                            case "single_byte_xor":
//                                decodedMessage = decodeSingleByteXOR(encodedInput);
//                                break;
//                            default:
//                                System.out.println("Unsupported method: " + method);
//                                return;
//                        }
//                        send("_" + decodedMessage + "_");
//                    }
//                    break;
//            }
//        }
//    }
//
//    private static String decodeSingleByteXOR(String encoded) {
//        byte[] bytes = hexStringToByteArray(encoded);
//        int key = findSingleByteXORKey(bytes);
//        byte[] decodedBytes = xorWithKey(bytes, key);
//        return new String(decodedBytes).replace(" ", "_");
//    }
//
//    private static byte[] hexStringToByteArray(String s) {
//        int len = s.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
//                    + Character.digit(s.charAt(i + 1), 16));
//        }
//        return data;
//    }
//
//    private static int findSingleByteXORKey(byte[] bytes) {
//        int bestKey = 0;
//        int bestScore = Integer.MIN_VALUE;
//
//        for (int key = 0; key < 256; key++) {
//            byte[] decoded = xorWithKey(bytes, key);
//            int score = scoreText(decoded);
//
//            if (score > bestScore) {
//                bestScore = score;
//                bestKey = key;
//            }
//        }
//
//        return bestKey;
//    }
//
//    private static byte[] xorWithKey(byte[] bytes, int key) {
//        byte[] result = new byte[bytes.length];
//        for (int i = 0; i < bytes.length; i++) {
//            result[i] = (byte) (bytes[i] ^ key);
//        }
//        return result;
//    }
//
//    private static int scoreText(byte[] text) {
//        String commonCharacters = "etaoin shrdlu";
//        int score = 0;
//        for (byte b : text) {
//            char c = (char) b;
//            if (commonCharacters.indexOf(Character.toLowerCase(c)) != -1) {
//                score++;
//            }
//        }
//        return score;
//    }
//}