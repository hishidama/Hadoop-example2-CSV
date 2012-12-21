. common.rc
mkdir -p $(dirname $SRC)
hadoop jar hadoop2.jar example.hadoop2.data.Create 160 $SRC
hadoop fs -cp $SRC $SRC_AZAREA/SalesEntity.txt
hadoop fs -put HhTable.txt $SRC_AZAREA
