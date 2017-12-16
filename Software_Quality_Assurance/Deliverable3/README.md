# CS1632_Deliverable3
1. University of Pittsburgh, 2017Fall, CS1632 Deliverable3, Selenium, JUnit Test, web testing  
2. Requirements can be found in https://github.com/laboon/CS1632_Fall2017/tree/master/deliverables/3
3. Compile with the following command: javac -cp .:./junit-4.12.jar:./hamcrest-core-1.3.jar:./selenium-java-2.52.0.jar:./selenium-server-standalone-2.52.0.jar *.java. Remember to replace :'s with ;'s if you are using Windows. If you are using Windows 7 you may need to put the classpath string (everywhere from the first . to the last .jar) in double quotes. Also note that you must ensure that your paths do not include any ~s, wildcards, or other shell-expanding characters (if in doubt, make absolute paths). javac will not expand these.  
4. Run with the following command: java -cp .:./junit-4.12.jar:./hamcrest-core-1.3.jar:./selenium-java-2.52.0.jar:./selenium-server-standalone-2.52.0.jar HerokuappTestRunner You should see 5 test failures. 
