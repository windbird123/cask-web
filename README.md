## Overview
* https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example
* https://github.com/Quafadas/cask-laminar
* https://github.com/sherpal/FlyIOScalaJVMDemo

## Install
You need to explicitly install the following software:

* sbt, as part of [getting started with Scala](https://docs.scala-lang.org/getting-started/index.html) (or if you prefer, through [its standalone download](https://www.scala-sbt.org/download.html))
* [Node.js](https://nodejs.org/en/)

Other software will be downloaded automatically by the commands below.


## Prepare

```bash
$ npm install
```


## Development

#### frontend
* 1st terminal (scala -> javascript)  
  ```bash
  $ sbt ~fastLinkJS
  ```

* 2nd terminal (vite dev server)
  ```bash
  $ npm run dev
  ```
  
아래 주소로 frontend 확인 가능함 
http://localhost:5173


#### backend
* 3rd terminal (backend api server)
  ```bash
  $ sbt ~backend/reStart
  ```
아래 주소로 Backend API 를 직접 확인 가능함
http://localhost:8080/hello


## Production (Basic)
#### frontend  
  ```bash
  # $npm ci
  $ npm run build
  ```

You can then find the built files in the dist/ directory. You will need an HTTP server, such as python3 -m http.server, to open the files, as Vite rewrites <script> tags to prevent cross-origin requests.

#### frontend 와 backend 를 각각 분리해 배포

## Production (Simple) 
One Jar 로 만들어 backend server 에서 html, js 서빙하기   

#### Dockerfile 의 ENTRYPOINT 확인
```
"caskweb.Main"
```

#### frontend 를 포함해 one-jar (app.jar) 로 만들기 
```
npm run build

rsync -av --delete dist backend/src/main/resources/dist
# rm -rf backend/src/main/resources/dist 
# mv dist backend/src/main/resources/dist 

sbt backend/assembly  # backend/target/scala-2.13/app.jar

# java -cp app.jar caskweb.Main
# http://localhost:8080 로 확인
```


#### Dockerfile 을 이용해 docker image 생성
```
docker build --tag demoscalaflyio .
docker run --rm -p 9000:8080 demoscalaflyio
```


---

### 개발 가이드
* main.js 에 의존성 있는(최종 index.html 에서 `link`(css)  혹은 `script`(js) 등으로 참조되어야 할) resource (js, css) 를 import 하면 `npm run build` 시 `dist/assets` 디렉토리에 모이게 된다. (js 파일은 bundling 됨)
* main.js 에서 import 하지 않지만 필요한 리소스(이미지 파일 등) 은 `frontend/public` 디렉토리에 넣는다.(vite.config.js 파일에서 `publicDir` 을 frontend/public 으로 설정했음)  `npm run build` 시 `dist/` 디렉토리에 모이게 된다. [vite-public-directory](https://vitejs.dev/guide/assets.html#the-public-directory)  
  * You should always reference public assets using root absolute path - for example, public/icon.png should be referenced in source code as /icon.png
  * Assets in public cannot be imported from JavaScript.
* main.js 에서 사용하는 @public 의 위치는 `frontend\target\scala-2.13\frontend-opt` 혹은 `frontend\target\scala-2.13\frontend-fastopt` 가 될 수 있다. 
