# beatoraja-ime-fix
A simple fix to beatoraja, designed to support auto switching of input methods on Windows.
## How to use
1. Download `beatoraja-ime-fix.zip` from releases page.
2. Unzip it to your beatoraja root directory.
3. Add `-javaagent:beatoraja-ime-injector.jar` to your beatoraja start command, like below.

    Example: `java -javaagent:beatoraja-ime-injector.jar -Xms1g -Xmx4g -jar beatoraja.jar`
4. Enjoy!
## Bug report
Please open a issue and provide your beatoraja console outputs.
