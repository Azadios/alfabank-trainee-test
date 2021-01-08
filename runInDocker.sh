docker build . -t abdulov-test-img
docker run -d -p 8080:8080 --name abdulov-test-cont abdulov-test-img
sleep 5
sensible-browser localhost:8080
