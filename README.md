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

	change to the branch you intend to build
	
	mvn deploy 
	
	copy the files created under target/mvn-repo to the mvn-repo branch or asterisk-java
	
	commit and push the changes.