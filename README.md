# CryptoFinal

Cryptography.java is executable from the command line, but needs the file path of the necessary jars to be explicitly included:

When compiling the code: javac -cp “path to jars” Cryptography.java
When running the code: java -cp “path to jars; path to Cryptography.class” Cryptography

Obviously the code only needs to be compiled once, but the class path must be specified at runtime each time

This class has the ability to take user input concerning a filename and whether they wish to upload or download to the blob. Uploading includes encryption.


