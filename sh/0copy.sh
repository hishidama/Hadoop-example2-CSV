#運用環境移行用の準備をする
BASEDIR=$(cd $(dirname $0);pwd)

cp -p $BASEDIR/../pig/*.txt $BASEDIR/pig
cp -p $BASEDIR/../hive/dist/*.txt $BASEDIR/hive

AFWDIR=$BASEDIR/../afw-windgate
cp -p $AFWDIR/target/afw-windgate-batchapps-1.0.jar $BASEDIR
