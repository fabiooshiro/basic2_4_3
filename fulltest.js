const
     fs = require('fs'),
     child_process = require('child_process');
var http = require('http');

var grails = child_process.exec('./grailsw');
var dots = 0;

function flushDots() {
  if (dots > 0) {
    console.log(new Date().toISOString().replace(/T|Z/g, ' '), 'working...');
  }
  dots = 0;
}

var hitCount = 0
function hitIndex() {
  console.log('hit /someClass/index', ++hitCount);
  http.request({
    host: 'localhost',
    port: '8080',
    path: '/basic2_4_3/someClass/index?t=' + hitCount
  }, function(response) {}).end();
}

function exitGrails() {
  grails.stdin.write('exit\n');
}

var runSend = false;
function runApp() {
  if(!runSend) {
    grails.stdin.write('run-app\n');
  }
  runSend = true;
}

var exitCode = 1;
var isOkCount = 0;
grails.stdout.on('data', function (data) {
  if (data.trim().match(/^\.+$/) === null) {
    flushDots();
    console.log('stdout: ' + data);
    if (data.indexOf('Daemon Started') !== -1) {
      runApp();
    } else if (data.indexOf('Server running. Browse to') !== -1) {
      setTimeout(function() { hitIndex() }, 1000);
      setTimeout(function() { hitIndex() }, 3000);
    } else if (data.indexOf('not ok') !== -1) {
      console.error("service scope prototype is not working!");
      exitGrails();
    } else if (data.indexOf('is ok') !== -1) {
      isOkCount++;
      if (isOkCount == 2) {
        console.log("service scope is working!");
        exitCode = 0;
        exitGrails();
      }
    }
  } else {
    dots++;
    if (dots % 79 === 0) {
      flushDots();
    }
  }
});

grails.stderr.on('data', function (data) {
  console.log('stderr: ' + data);
});
grails.on('exit', function (code) {
  console.log('Child process exited with exit code ', code);
  process.exit(exitCode);
});
