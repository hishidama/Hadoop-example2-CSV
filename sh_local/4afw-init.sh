BASEDIR=$(cd $(dirname $0);pwd)
AFWDIR=$BASEDIR/../afw-windgate
echo $AFWDIR
cd $AFWDIR
mvn package
cd $ASAKUSA_HOME/batchapps
jar xf $AFWDIR/target/afw-windgate-batchapps-1.0.jar
rm -rf META-INF
find Example2AfwBatch -name "*.sh" | xargs chmod +x

echo 'asakusa.propertiesを変更していない場合は、$ASAKUSA_HOME/windgate/profile/asakusa.propertiesのresource.local.basePathを/tmp/hadoop2に変更しておく必要がある'

