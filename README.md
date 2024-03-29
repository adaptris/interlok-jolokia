# interlok-jolokia

[![GitHub tag](https://img.shields.io/github/tag/adaptris/interlok-jolokia.svg)](https://github.com/adaptris/interlok-jolokia/tags)
[![license](https://img.shields.io/github/license/adaptris/interlok-jolokia.svg)](https://github.com/adaptris/interlok-jolokia/blob/develop/LICENSE)
[![Actions Status](https://github.com/adaptris/interlok-jolokia/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/adaptris/interlok-jolokia/actions)
[![codecov](https://codecov.io/gh/adaptris/interlok-jolokia/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-jolokia)
[![CodeQL](https://github.com/adaptris/interlok-jolokia/workflows/CodeQL/badge.svg)](https://github.com/adaptris/interlok-jolokia/security/code-scanning)
[![Known Vulnerabilities](https://snyk.io/test/github/adaptris/interlok-jolokia/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/adaptris/interlok-jolokia?targetFile=build.gradle)
[![Closed PRs](https://img.shields.io/github/issues-pr-closed/adaptris/interlok-jolokia)](https://github.com/adaptris/interlok-jolokia/pulls?q=is%3Apr+is%3Aclosed)

Manage Interlok with JMX via an HTTP API.


## Installation

Drop the **interlok-jolokia.jar** built from this project and its dependencies **jolokia-core.jar**, **jolokia-jsr160** and **json-simple** into your Interlok **lib** directory, then modify your **bootstrap.properties** to make sure the managementComponents property contains all of "**jetty**", "**jmx**", and "**jolokia**".

```
managementComponents=jetty:jmx:jolokia
```

## Running

Using your favourite HTTP GET/POST tool, make a POST request to the running Interlok Jolokia instance at `http://localhost:8081/jolokia/`:

### Read

Request

```
curl --location --request POST 'http://localhost:8081/jolokia/' --header 'Content-Type: application/json' --header 'Accept: application/json' \
--data-raw '{
  "type" : "READ",
  "mbean" : "com.adaptris:type=Adapter,id=MyInterlokInstance",
  "attribute": "ComponentState"
}'
```

Response

```
{
  "request": {
    "mbean": "com.adaptris:id=MyInterlokInstance,type=Adapter",
    "attribute": "ComponentState",
    "type": "read"
  },
  "value": "StartedState",
  "timestamp": 1612416801,
  "status": 200
}
```

### Exec

Request

```
curl --location --request POST 'http://localhost:8081/jolokia/' --header 'Content-Type: application/json' --header 'Accept: application/json' \
--data-raw '{
  "type" : "EXEC",
  "mbean" : "com.adaptris:type=Adapter,id=MyInterlokInstance",
  "operation": "requestStart()"
}'
```

Response

```
{
  "request": {
    "mbean": "com.adaptris:id=MyInterlokInstance,type=Adapter",
    "type": "exec",
    "operation": "requestStart()"
  },
  "value": null,
  "timestamp": 1612416801,
  "status": 200
}
```

### Search

Request

```
curl --location --request POST 'http://localhost:8081/jolokia/' --header 'Content-Type: application/json' --header 'Accept: application/json' \
--data-raw '{
  "type" : "SEARCH",
  "mbean" : "com.adaptris:type=Workflow,adapter=MyInterlokInstance,channel=*,id=*"
}'
```

Response

```
{
  "request": {
    "mbean": "com.adaptris:adapter=MyInterlokInstance,channel=*,id=*,type=Workflow",
    "type": "search"
  },
  "value": [
    "com.adaptris:adapter=MyInterlokInstance,channel=Channel1,id=Workflow11,type=Workflow",
    "com.adaptris:adapter=MyInterlokInstance,channel=Channel1,id=Workflow12,type=Workflow",
    "com.adaptris:adapter=MyInterlokInstance,channel=Channel2,id=Workflow21,type=Workflow",
    "com.adaptris:adapter=MyInterlokInstance,channel=Channel2,id=Workflow22,type=Workflow",
    "com.adaptris:adapter=MyInterlokInstance,channel=Channel3,id=Workflow31,type=Workflow",
    "com.adaptris:adapter=MyInterlokInstance,channel=Channel3,id=Workflow33,type=Workflow"
  ],
  "timestamp": 1612419632,
  "status": 200
}
```

## Jolokia

You can read more about jolokia protocol at [https://jolokia.org/reference/html/protocol.html#post-request](https://jolokia.org/reference/html/protocol.html#post-request)

Get request can also be used for simple queries ([https://jolokia.org/reference/html/protocol.html#get-request](https://jolokia.org/reference/html/protocol.html#get-request)) but Jolokia recommend to use post requests.

## Extra config

Basic Authentication can be added with the following properties in **bootstrap.properties**

```
jolokiaUsername=admin
jolokiaPassword=admin
```

The port can be change with the following property in **bootstrap.properties**

```
jolokiaPort = 8081
```

The context path can be change with the following property in **bootstrap.properties**

```
jolokiaContextPath=/jolokia
```

