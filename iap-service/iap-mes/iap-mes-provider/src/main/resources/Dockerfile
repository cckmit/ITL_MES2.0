FROM java:8
ENV ARTIFACTID iap-mes-provider
ENV ARTIFACTVERSION 1.0
ENV HOME_PATH /home
WORKDIR $HOME_PATH
ADD /$ARTIFACTID-$ARTIFACTVERSION.jar $HOME_PATH/$ARTIFACTID.jar
ENTRYPOINT ["java", "-jar", "iap-mes-provider.jar"]
