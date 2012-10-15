TflySocketServer
================

A simple socket server with sequence synchronization
Usage:
ant clean compile jar run &
runServer.sh runs the above command

by default the server runs on port 4567, to change the port look at this section in build.xml
<target name="run">
        <java jar="build/jar/TflyServer.jar" fork="true">
            <arg line="4567"/>  ------------->> change the port number here
        </java>
    </target>
