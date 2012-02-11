BASEDIR=$(cd $(dirname $0);pwd)
hadoop fs -put $BASEDIR/hh_table.txt .
