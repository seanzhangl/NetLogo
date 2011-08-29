package org.nlogo.prim.etc

import org.nlogo.nvm.{ Command, Context, Syntax }

class _stamp extends Command {
  override def syntax =
    Syntax.commandSyntax("-T-L", true)
  override def perform(context: Context) {
    world.stamp(context.agent, false)
    context.ip = next
  }
}
