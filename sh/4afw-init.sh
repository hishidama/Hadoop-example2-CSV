BASEDIR=$(cd $(dirname $0);pwd)
cd $ASAKUSA_HOME/batchapps
jar xf $BASEDIR/afw-windgate-batchapps-1.0.jar
rm -rf META-INF
find Example2AfwBatch -name "*.sh" | xargs chmod +x

echo 'asakusa.propertiesを変更していない場合は、$ASAKUSA_HOME/windgate/profile/asakusa.propertiesのresource.local.basePathを/tmp/hadoop2に変更しておく必要がある'

