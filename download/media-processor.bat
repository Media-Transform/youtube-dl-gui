@echo off

set appTitle="Media-Processing"
set appJar="media-processing.jar"
echo Running %appTitle%

::
:: Find the javaw in the path.  If it doesn't exist
:: then exit the code with a message.
::
set found =
for %%X in (javaw.exe) do (set found=%%~$PATH:X)

if not defined found (
	echo "Could not find java.exe in your path.  Please make sure to install it.
	pause
	exit /b 1
)

call START /B "%appTitle%" javaw.exe -jar %appjar%