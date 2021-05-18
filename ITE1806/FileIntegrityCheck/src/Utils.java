import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Utils {
    // Could be designed way better... But hey, this is a quickie :)

    /**
     * Writes an entry to the error log.
     * @param error Error message to write
     */
    public static void writeErrorLog(String error){
        writeErrorLog(error, false);
    }

    /**
     * Writes an entry to error.log.
     * @param error Error message to write
     * @param writeToConsole Optionally prints the error to console
     */
    public static void writeErrorLog(String error, Boolean writeToConsole){
        if(writeToConsole)
            System.out.println(error);
        try{
            writeToFile(error, "error.log", true);
        } catch (IOException ex){
            System.out.println(error);
            System.out.println("Unable to write to error-log: " + ex.getMessage());
        }
    }

    /**
     * Writes a message to file. If file exists, open file and append.
     * If not, create a new file and write to that
     * @param data Data to write to file.
     * @param filePath Path to file to write to.
     * @throws IOException May be unable to open/write to file
     */
    public static void writeToFile(String data, String filePath) throws IOException {
        writeToFile(data, new File(filePath), true);
    }

    /**
     * Writes a message to file. If file exists, open file and append.
     * If not, create a new file and write to that
     * @param data Data to write to file.
     * @param filePath File-object pointing to file to write to.
     * @throws IOException May be unable to open/write to file
     */
    public static void writeToFile(String data, File filePath) throws IOException {
        writeToFile(data, filePath, true);
    }

    /**
     * Writes a string to file.
     * @param data Data to write to file.
     * @param filePath Path to file to write to.
     * @param append Append data to file if true.
     * @throws IOException May be unable to open/write to file
     */
    public static void writeToFile(String data, String filePath, boolean append) throws IOException {
        writeToFile(data, new File(filePath), append);
    }

    /**
     * Writes a message to file. If file exists, it appends the message on a new line.
     * If not, creates a new file, then writes to that.
     * @param data String to write to file.
     * @param filePath File-object pointing to file to write to.
     * @param append Append data to file if true.
     * @throws IOException May be unable to open/write to file
     */
    public static void writeToFile(String data, File filePath, boolean append) throws IOException {
        PrintWriter writer = null;
        if (filePath.exists() && !filePath.isDirectory()) {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, append)));
        } else {
            writer = new PrintWriter(filePath);
        }
        writer.append(data);
        writer.close();
    }

    /**
     * Searches a directory-tree and creates a list of filepaths.
     * @param directory Directory to search recursivly
     * @return ArrayList<String> or null
     */
    public static ArrayList<String> searchDirectory(File directory){
        if(directory == null) return null;
        ArrayList<String> list = new ArrayList<>();

        //Search recursively through a directory
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if(file.isDirectory()){
                //if a directory, search through that
                list.addAll(searchDirectory(file));
            } else {
                //if not, add file to path-list
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * Writes a hashmap to file in the format key;value;
     * @param map Hashmap to write to file
     * @param filePath Where to store the hashmap
     * @throws IOException Unable to write to file
     */
    public static void writeHashMapToFile(HashMap map, File filePath) throws IOException {
        filePath.getParentFile().mkdirs();

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)));

        //Write the hash-map to a file
        for ( Object key : map.keySet() ) {
            writer.append(String.format("%s;%s;%n",key.toString(), map.get(key)));
        }
        writer.close();
    }

    /**
     * Reads a file with the format key;value; and creates a hashmap
     * @param filePath Path to file to read
     * @return HashMap<String, String>
     * @throws IOException May be unable to read to the logfile
     */
    public static HashMap<String, String> readHashMapFromFile(File filePath) throws IOException {
        HashMap<String, String>  map = new HashMap<>();
        String[] data;

        //Does the file exist?
        if(filePath.exists() && !filePath.isDirectory()){
            //read through it line by line, and put it into a has-map
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            for(Object line : reader.lines().toArray()) {
                //split into key-value pair
                data = line.toString().split(";");
                map.put(data[0], data[1]);
            }
            reader.close();
            return map;
        } else {
            // write an error report if we can't open the file
            Utils.writeErrorLog(String.format("%s: Not a file!", filePath.getPath()), false);
            return null;
        }
    }

    /**
     * Calculates a sha256-hash of file
     * @param file File to hash
     * @return String containing a sha256-hash of file or null
     * @throws IOException File may not exist
     */
    public static String getSha256(File file) throws IOException {
        if(file.exists() && !file.isDirectory()) {
            return DigestUtils.sha256Hex(new FileInputStream(file)); // <-- Black magic happens here...
        } else {
            return null;
        }
    }

    /**
     * Generates a signature for a file and writes it to disk, along with accompanying public key
     * @param file File to generate signature for
     * @param keystoreName Path to keystore containing private key
     * @param keystorePass Password to keystore
     * @param keystoreAlias Alias/Username for the keystore
     * @return True if we managed to execute all tasks
     * @throws KeyStoreException Error in key-store
     * @throws IOException Error reading/writing file
     * @throws UnrecoverableKeyException Can not recover key from keystore
     * @throws NoSuchAlgorithmException wrong key-algorithm
     * @throws InvalidKeyException wrong key
     * @throws SignatureException signature failed
     * @throws CertificateException invalid certificate
     */
    public static boolean sign(File file, String keystoreName, String keystorePass, String keystoreAlias) throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, CertificateException {
        // Open keystore
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream ksfis = new FileInputStream(keystoreName);
        BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
        ks.load(ksbufin, keystorePass.toCharArray());

        // Retrieve private key
        PrivateKey privKey = (PrivateKey) ks.getKey(keystoreAlias, keystorePass.toCharArray());

        // Create a Signature object and initialize it with the private key
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privKey);

        // Update buffer with data to sign
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[1024];
        int len;
        while (input.available() != 0) {
            len = input.read(buffer);
            signature.update(buffer, 0, len);
        }
        input.close();

        // write generated signature to file
        BufferedOutputStream signOut = new BufferedOutputStream(new FileOutputStream(file.getParent()+File.separator+"hashes.sig"));
        signOut.write(signature.sign());
        signOut.close();

        // Write public-key to file
        BufferedOutputStream keyOut = new BufferedOutputStream(new FileOutputStream(file.getParent()+File.separator+"pubkey.key"));
        keyOut.write(ks.getCertificate(keystoreAlias).getPublicKey().getEncoded());
        keyOut.close();

        return true;
    }

    /**
     * Verifies a file against a signature file, with given public key
     * @param dataFile File to verify
     * @param signFile File containing signature of dataFile
     * @param pubKeyFile Public key to the corresponding privatekey used to create signature
     * @return True if datafile successfully passes signature-check; False otherwise.
     * @throws IOException Error reading/writing file
     * @throws InvalidKeySpecException Wrong key type
     * @throws NoSuchAlgorithmException Wrong key-algorithm
     * @throws InvalidKeyException Wrong key
     * @throws SignatureException Signature failed
     */
    public static boolean verifySignature(File dataFile, File signFile, File pubKeyFile) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        // Read public-key-bytes to byte-array
        BufferedInputStream pubKeyInput = new BufferedInputStream(new FileInputStream(pubKeyFile));
        byte[] encKey = pubKeyInput.readAllBytes();
        pubKeyInput.close();

        // Create public-key from byte-array
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encKey));

        // Read signature-file to byte-array
        FileInputStream sigFileInput = new FileInputStream(signFile);
        byte[] sigToVerify = sigFileInput.readAllBytes();
        sigFileInput.close();

        // Initialize signature wit public-key
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(pubKey);


        // Update buffer with data to check
        BufferedInputStream dataInput = new BufferedInputStream(new FileInputStream(dataFile));
        byte[] buffer = new byte[1024];
        int len;
        while (dataInput.available() != 0) {
            len = dataInput.read(buffer);
            signature.update(buffer, 0, len);
        };
        dataInput.close();

        //Verify if everything checks out
        return signature.verify(sigToVerify);
    }
}