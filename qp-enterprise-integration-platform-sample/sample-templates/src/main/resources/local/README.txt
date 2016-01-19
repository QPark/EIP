Platform version: xxx-application.version-xxx
Domain version: xxx-domain.version-xxx
Build Date: xxx-build.time-xxx

Installation command on local environment (windows):

tcruntime-instance.bat create iss-library -f templates/xxx-application.name-xxx-base-xxx-application.version-xxx/conf/template-fragment.properties -t bio -t ajp -t insight -t xxx-application.name-xxx-base-xxx-application.version-xxx -t xxx-application.name-xxx-xxx-environment.name-xxx-xxx-application.version-xxx -t xxx-application.name-xxx-xxx-environment.name-xxx-user-xxx-application.version-xxx -v 8.0.26.B.RELEASE

Or without insight:
tcruntime-instance.bat create iss-library -f templates/xxx-application.name-xxx-base-xxx-application.version-xxx/conf/template-fragment.properties -t bio -t ajp -t xxx-application.name-xxx-base-xxx-application.version-xxx -t xxx-application.name-xxx-xxx-environment.name-xxx-xxx-application.version-xxx -t xxx-application.name-xxx-xxx-environment.name-xxx-user-xxx-application.version-xxx -v 8.0.26.B.RELEASE
