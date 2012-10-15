TflySocketServer
================

A simple socket server with sequence synchronization
Running Server:
1.  ant clean compile jar run &
    runServer.sh runs the above command, it cleans the old classes, compiles a fresh version of them, packages all of them in a jar and then runs the jar.

2.  by default the server runs on port 4567, to change the port look at this section in build.xml
    <target name="run">
        <java jar="build/jar/TflyServer.jar" fork="true">
            <arg line="4567"/>  ------------->> change the port number here
        </java>
    </target>

Connecting to the server:

telnet localhost <port number>

Assumptions:
1. You need to have atleast 2 words in the string to sync
so when input a single number, the output would be that number and normal incremented seq number.
eg:
02817a-mapte:Ticketfly mapte$ telnet localhost 4567
Connected to localhost.
Escape character is '^]'.
try
yrt 59
hi 
ih 60
789
987 61
1
1 62

2. When the input is any word (a number or set of letters) and a number separated by space, that signals syncing
eg:
02817a-mapte:Ticketfly mapte$ telnet localhost 4567
Connected to localhost.
Escape character is '^]'.
hi
ih 63
there
ereht 64
64 77
46 78
hi
ih 79
there
ereht 80

3. the character '.' signals end of session from the client, the connection will be closed by the server.

