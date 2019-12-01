FROM amazonlinux:2 as download

RUN /bin/bash -c "yes | yum install tar gzip" && \
    curl -o /opt/graalvm-ce-19.2.1.tgz \
        -L https://github.com/oracle/graal/releases/download/vm-19.2.1/graalvm-ce-linux-amd64-19.2.1.tar.gz && \
    # curl -o /opt/graalvm-ce-19.3.0.tgz \
    #    -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-19.3.0/graalvm-ce-java8-linux-amd64-19.3.0.tar.gz && \
    /bin/bash -c "(cd /opt; tar xvzf graalvm-ce-19.2.1.tgz)"

FROM amazonlinux:2

COPY --from=download /opt/graalvm-ce-19.2.1 /opt/graalvm-ce/

RUN amazon-linux-extras enable corretto8 && \
    yum clean metadata && \
    /bin/bash -c "yes | yum install java-1.8.0-amazon-corretto-devel which gcc glibc-devel zlib-devel" && \
    /bin/bash -c 'GRAALVM_HOME=/opt/graalvm-ce JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto.x86_64 /opt/graalvm-ce/bin/gu install native-image'

WORKDIR /work
