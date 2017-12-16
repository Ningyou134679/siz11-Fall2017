# CS1632_Deliverable4
1. University of Pittsburgh, 2017Fall, CS1632 Deliverable4, Performance Testing, VisualVM  
2. Requirements can be found in https://github.com/laboon/CS1632_Fall2017/tree/master/deliverables/4
3. Compile with the following command: `javac -cp ./junit-4.12.jar:./hancrest-core-1.3.jar:./mockito-core-1.10.19.jar:./objenesis-2.4.jar *java`. Remember to replace :'s with ;'s if you are using Windows. If you are using Windows 7 you may need to put the classpath string (everywhere from the first . to the last .jar) in double quotes. Also note that you must ensure that your paths do not include any ~s, wildcards, or other shell-expanding characters (if in doubt, make absolute paths). javac will not expand these.  
4. Run with the following command: `java -cp .:./junit-4.12.jar:./hamcrest-core-1.3.jar:./mockito-core-1.10.19.jar:./objenesis-2.4.jar TestRunner` You should see 0 test failures. 
5. Compile and run Element.java: `javac Element.java`; `java Element <.txt>`
6. sample_input.txt is the default test file in the requirement. small.txt is use for fast testing. large.txt is used to have enough time to view the program in VisualVM.
