class Foo {
  bar() { console.log("hello"); }
  bar() { console.log("goodbye"); }
}

var foo = new Foo();
foo.bar(); // goodbye