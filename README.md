# note-server

# deploy
```
mvn package
mkdir /home/beyond/software/bin/note-server
cp target/note-server-0.1.jar /home/beyond/software/bin/note-server/
```

start shell
```
if [ -n "$1" ]; then
    if [ "$1" = 'stop' ]; then
        echo "stopping ..."
        kill $(cat /home/beyond/software/bin/note-server/note-server.pid)
        rm /home/beyond/software/bin/note-server/note-server.pid
        echo "stopped"
    fi
    if [ "$1" = 'restart' ]; then
        echo "restarting ..."
        kill $(cat /home/beyond/software/bin/note-server/note-server.pid)
        rm /home/beyond/software/bin/note-server/note-server.pid
        nohup java -Xms32m -Xmx256m -jar /home/beyond/software/bin/note-server/note-server-0.1.jar > /home/beyond/software/bin/note-server/note-server.log 2>&1 &
        echo $! > /home/beyond/software/bin/note-server/note-server.pid
        echo "restarted"
    fi
else
    if [ -f /home/beyond/software/bin/note-server/note-server.pid ]; then
        echo "server is running ..."
    else
        echo "starting ..."
        nohup java -Xms32m -Xmx256m -jar /home/beyond/software/bin/note-server/note-server-0.1.jar > /home/beyond/software/bin/note-server/note-server.log 2>&1 &
        echo $! > /home/beyond/software/bin/note-server/note-server.pid
        echo "started"
    fi

fi
```