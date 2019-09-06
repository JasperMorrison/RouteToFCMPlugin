# RouteToFCMPlugin
# How to add a plugin to openfire xmpp server:
1. Download openfire
2. cd openfire dir, run: mvn verify. Set the proxy of mvn if connect failed
3. clone this repo to the Openfire\plugins
4. build the plugin: mvn vefiry -f plugins\RouteToFCMPlugin\pom.xml
5. run openfire: distribution\target\distribution-base\bin\openfire.bat
