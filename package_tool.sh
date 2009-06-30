#!/bin/sh

updateVersionArg="--updateVersion"
buildSourceArchiveArg="--buildSourceArchive"

printUsage() {
	echo "usages:\n  $0 $updateVersionArg [version]\n  $0 $buildSourceArchiveArg [version]";
	exit;
}

if [ $# -ne 1 -a $# -ne 2 ]; then
	printUsage;
fi

updateVersion() {
	intversion=`echo $version | sed "s/\\./0/g" | sed -E "s/^0+//"`
	echo "<!ENTITY FBReaderVersion   \"$version\">" > data/formats/fb2/FBReaderVersion.ent
	sed "s/@INTVERSION@/$intversion/" platform/android/AndroidManifest.xml.pattern | sed "s/@VERSION@/$version/" > platform/android/AndroidManifest.xml
}

buildSourceArchive() {
	updateVersion
  rm -rf $dir $archive
  mkdir $dir
  cp -r data icons src platform native manifest.mf build.xml $0 VERSION $dir
  rm -rf `find $dir -name .svn`
  zip -rq $archive $dir/*
  rm -rf $dir
}

if [ $# -eq 2 ]; then
	version=$2;
	echo $version > VERSION
else
	version=`cat VERSION`
fi

dir=FBReaderJ-sources-$version
archive=FBReaderJ-sources-$version.zip


case $1 in
	$updateVersionArg)
		updateVersion;
		;;
	$buildSourceArchiveArg)
		buildSourceArchive;
		;;
	*)
		printUsage;
		;;
esac