/**
  * Created by k_higuchi on 2017/08/11.
  */
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.{Global, Phase}

class DivByZeroCheckPlugin(val global: Global) extends Plugin {

  import global._

  val name = "divbyzerocheck"
  val description = "checks for division by zero"
  val components = List(Component)

  private[DivByZeroCheckPlugin] object Component extends PluginComponent {

    val global: DivByZeroCheckPlugin.this.global.type = DivByZeroCheckPlugin.this.global
    val runsAfter = List("refchecks")
    val phaseName = DivByZeroCheckPlugin.this.name

    def newPhase(_prev: Phase) = new DivByZeroCheckPhase(_prev)

    class DivByZeroCheckPhase(prev: Phase) extends StdPhase(prev) {

      override def name = DivByZeroCheckPlugin.this.name

      def apply(unit: CompilationUnit): Unit = {
        for {
          tree @ Apply(Select(rcvr, nme.DIV), List(Literal(Constant(0)))) <- unit.body
          if rcvr.tpe <:< definitions.IntClass.tpe
        } reporter.error(tree.pos, "definitely division by zero")
      }

    }

  }

}
