################################
### Builder image
################################
FROM node:8.16-alpine AS builder

ENV VERSION=version2.0

RUN apk add --no-cache git

RUN git clone https://github.com/ontio/ontology-explorer.git && \
    cd /ontology-explorer && \
    git checkout $VERSION

WORKDIR /ontology-explorer/front-end/

RUN npm install && \
    npm run build


##################################
##### Runner image
##################################
FROM nginx:1.17-alpine

COPY --from=builder /ontology-explorer/front-end/dist/ /usr/share/nginx/html/
