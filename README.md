# study-compiler-plugin

とりあえず以下の公式ドキュメントを参照  
古くてもうメンテされていないけど新しいドキュメントがなさそう

Writing Scala Compiler Plugins  
http://www.scala-lang.org/old/node/140

マルチプロジェクト構成  
作成したプラグインはjarにする必要があるため、  
プラグインを使うプロジェクトとプラグインを作成するプロジェクトに分割  

ディレクトリ構成（一部省略）

```
study-compiler-plugin/
  src/
    client/ [compiler-plugin-client]
      <プラグインを使うクライアントプロジェクト>
    plugin/ [compiler-plugin]
      <プラグインを提供するサーバープロジェクト>
```

## scala-compilerの依存性解決
※このプロジェクトのbuild.sbtで解決済みなので追加不要  
(プラグインを作成する方のプロジェクトにだけ設定済み)

```scala:build.sbt
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.12.3"
```

メモ
- コンパイラ・プラグインAPIは`scala-compiler`というプロジェクトに入っている(※v2.12.3時点)
- `scala-compiler`はScalaのプロジェクトの一つだけど、デフォルトではScalaに同梱されない(※v2.12.3時点)

## ${compiler-plugin}.jarの作成
`compiler-plugin`というprojectのjarを作る

```sbt
// sbtは起動している前提
> project compiler-plugin
> package
```

メモ
- jarのトップレベルに `scalac-plugin.xml` ファイルが必要
- 該当ファイルはresourcesディレクトリに配置、packageの際に読み込むように設定済み

内容
```xml:scalac-plugin.xml
<plugin>
    <name>divbyzerocheck</name>
    <classname>DivByZeroCheckPlugin</classname>
</plugin>
```

## プラグインを使う（コンパイルする）
`compiler-plugin-client`にコードを書いてコンパイルする

```sbt
// sbtは起動している前提
> project compiler-plugin-client
> compile
```

メモ
- コンパイルするだけなのは、すでに作成したjarをscalacのオプション指定するなどbuild.sbtに設定しているため

## Example:ゼロ除算のコンパイルエラー
冒頭のドキュメントにある通り、ゼロ除算をコンパイルエラーにしてみる　　
コンパイルすると以下のようなエラーが出る(pathは適当にしている)

```sbt
> compile
[info] Compiling 1 Scala source $PROJECT_PATH/study-compiler-plugin/src/client/target/scala-2.12/classes...
[error] $PROJECT_PATH/study-compiler-plugin/src/client/main/scala/Main.scala:7: definitely division by zero
[error]   val amount = five / 0
[error]                     ^
[error] one error found
[error] (compiler-plugin-client/compile:compileIncremental) Compilation failed
[error] Total time: 1 s, completed 2017/08/11 13:45:16
```
