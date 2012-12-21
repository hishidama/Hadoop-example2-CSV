BASEDIR=$(cd $(dirname $0);pwd)
. common.rc
mkdir -p $(dirname $SRC)
hadoop jar $BASEDIR/../sh/hadoop2.jar example.hadoop2.data.Create 1 $SRC
mkdir -p $SRC_AZAREA/SalesEntity.txt
cp $SRC $SRC_AZAREA/SalesEntity.txt
cp $BASEDIR/../sh/HhTable.txt $SRC_AZAREA
