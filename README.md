# csc413-SecondGame

## Student Name  : Duy Nguyen
## Student ID    : 917446249


## src folder is to be used to store source code only.

## resources folder is to be used to store the resources for your project only. This includes images, sounds, map text files, etc.

## jar folder is used to store the built jar of your term-project. NO SOURCE CODE SHOULD BE IN THIS FOLDER. DOING SO WILL CAUSE POINTS TO BE DEDUCTED

## IMPORTING AND RUNNING THE PROJECT:
NOTE: The second game chosen to be built is Super Rainbow Reef. It is missing a way to keep high scores and the Pop sprite may occasionally clip into an unbreakable object, and Pop will either lose a life or need a restart. This is something I'm still trying to figure out.

1. First, clone the repository either by using the terminal/command prompt or using the Github desktop app. The easier method would be the Github app. Click the green button in the repository that says "Clone or download" and copy the HTTPS link. Open the app and select the File option in the upper righthand corner and select Clone repository... andgo to the URL tab and paste the link. Select the file path that works best for you and click Clone.

2. The following instructions are for IntelliJ. Open IntelliJ and click the Import Project button. Then find where you cloned the repository and select the file then hit OK.

3. A window should pop up and "Create project from existing sources" should be selected then hit Next. Afterwards, the Project Name does not need to be changed. Make sure the Project Format is .idea then hit Next.

4. There should be a checklist with only one option that ends with \src. Make sure it's checked then hit Next.

5. Two box lists labeled Libraries and Library contents should have only 1 thing under it. The Library selected should be the same name as the repository name, and the Library Contents should be a .jar file that also has the same name selected. If so, hit Next. Then there will be Modules with Module dependencies, only one module should be there with no dependencies selected and it has the same name as the repository. Hit Next if this is true.

6. Then you will be prompted to choose an SDK. Choose 12 if it is available, if not then please go to the Java/Oracle site and download SDK 12. Go ahead and hit Next and then it will ask about framework but there shouldn't be any so just hit Finish.

7. To run the game, ensure that the folders are correctly marked by hitting File and checking the Project Structure then the Modules tab. The resources file should be marked as Resources and the src folder should be marked as Sources. Ideally, also check that the Project compiler output is set to the out folder's file path in the project folder, or wherever works best for you.

8. If everything is already fine, to actually run the game, open the jar folder and run the jar file by right-clicking it and selecting the run option or hitting the play button or open the src folder and select the GameWorld file and run it by doing the same thing. Alternatively, you can open the terminal/command line and change directories to where the jar file is located and run the jar that way, with the command: java -jar csc413-secondgame-duynguyen2.jar


## How to play:
Player Controls: Left and right arrow keys for movement, the W, A keys to control the shooting angle and Spacebar to shoot.
Once the player's lives reach 0, there will be a loss screen or all of the Big Leg sprites are gone, the game immediately goes to Congratulations screen. To replay, close the game window and re-run it.
