

ARG BUILD_NL_VERSION=VERSION_TO_REPLACE

FROM  neotys/neoload-loadgenerator:$BUILD_NL_VERSION

WORKDIR /neoload

RUN ls -l

ENV user=neoload
ENV group=0
VOLUME /etc/secrets
USER root

RUN mkdir bin
ADD https://storage.googleapis.com/genesis-public/cli/qa/bin/linux/amd64/whiteblock bin/wb

RUN ls -l /neoload/bin
RUN chown neoload /neoload/bin/wb
RUN chmod 755 /neoload/bin/wb


USER neoload

ENV  PATH="$PATH:/neoload/bin"
