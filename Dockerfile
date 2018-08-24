FROM openjdk:8
RUN apt-get update && apt-get install make

ADD . /usr/local/redisproxy
WORKDIR /usr/local/redisproxy
RUN make
#RUN make test
#RUN make run_test
#CMD ["java", "-cp", "bin:lib/jedis-2.9.0.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:lib/commons-pool2-2.6.0.jar::.", "Main"]


