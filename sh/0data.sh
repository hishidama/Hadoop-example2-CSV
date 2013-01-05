. common.rc
mkdir -p $(dirname $SRC)
hadoop jar hadoop2.jar example.hadoop2.data.Create 160 $SRC
