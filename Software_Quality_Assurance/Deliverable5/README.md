# CS1632_Deliverable5  
1. University of Pittsburgh, 2017Fall, CS1632 Deliverable5, Static Analysis, Findbugs, Checkstyle, Covering Array  
2. Requirement and course files can be found at: https://github.com/laboon/CS1632_Fall2017/tree/master/deliverables/5  
3. Compile with the following command: `javac -cp ./junit-4.12.jar:./hancrest-core-1.3.jar:./mockito-core-1.10.19.jar:./objenesis-2.4.jar *java.` Remember to replace :'s with ;'s if you are using Windows. If you are using Windows 7 you may need to put the classpath string (everywhere from the first . to the last .jar) in double quotes. Also note that you must ensure that your paths do not include any ~s, wildcards, or other shell-expanding characters (if in doubt, make absolute paths). javac will not expand these. 
4. Run the program with the following command: `java Pairwise <2 or more arguments>`
5. Run the test file with the following command: `java -cp .:./junit-4.12.jar:./hamcrest-core-1.3.jar:./mockito-core-1.10.19.jar:./objenesis-2.4.jar TestRunner` You should see 0 test failures.
6. Run checkstyle with the following command: `java -jar ./checkstyle-7.0-all.jar -c ./google_checks_modified.xml ./*.java`
7. Download findbug at http://findbugs.sourceforge.net/, click on file->create file->add Pairwise.class as class path, and run.
