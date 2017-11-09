docker build --tag green-build .
docker run -d --name green green-build true
docker cp green:/usr/src/app/greenpepper-confluence/greenpepper-confluence5-plugin/target/greenpepper-confluence5-plugin-4.0-SNAPSHOT-complete.jar .

