BASEDIR=$(cd $(dirname $0);pwd)

hive -f $BASEDIR/hive/create.txt
