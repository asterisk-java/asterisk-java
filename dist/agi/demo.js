// countdown
for (var i = 10; i >= 0; i--)
{
    channel.sayNumber(i);
}

channel.streamFile('beep');

// read a few dtmf digits
var code;
while ((code = channel.waitForDigit(10000)) != 0)
{
  var digit = String.fromCharCode(code);
  if (digit == '#' || digit == '*')
  {
    break;
  }
  channel.sayDigits(digit);
}

// beep to say good bye ;)
channel.streamFile('beep');

