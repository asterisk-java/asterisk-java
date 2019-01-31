This is the Maven Repository for asterisk java as of asterisk-java-2.0.3

To use this repository add these entries to your pom.xml

	<repositories>
		<repository>
			<id>mvn-repo</id>
            <url>https://raw.githubusercontent.com/asterisk-java/asterisk-java/mvn-repo</url>
 			<releases>
				<enabled>true</enabled>
			</releases>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>
	</repositories>

To add a new version to this repository:

	switch to the 'mvn repo' branch

	The existing repo in the 'mvn repo' branch needs to be copied into the target directory before the build, so that the meta data will be correctly updated  
	
	mkdir -p target/mvn-repo
	cp -R org target/mvn-repo
	
	change to the branch you intend to build (the target directory should be preserved as it isn't tracked by git)
		
	mvn deploy
	
	change back to the 'mvn repo' again the target directory should be preserved
		
	copy the files created under target/mvn-repo to the mvn-repo branch of asterisk-java
	
	cp -R target/mvn-repo/org .
	
	commit and push the changes.
