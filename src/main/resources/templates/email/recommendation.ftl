[kellegucker] ${ recommendations?size } investment recommendation(s)

---

<#list recommendations as r>
* ${r.stock.name}

  <#if r.type == "BUY">
  ${r.type} below ${r.stock.triggers.buy} (current ${r.stock.quotes.close}).
  <#else>
  ${r.type} above ${r.stock.triggers.sell} (current ${r.stock.quotes.close}).
  </#if>

  https://finance.yahoo.com/q?s=${r.stock.symbol}

</#list>

Have fun with it!

---

Die Gucker.