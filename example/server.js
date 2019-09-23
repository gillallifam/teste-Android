const express = require('express');
const fileUpload = require('../lib/index');
const app = express();

const PORT = 8000;
app.use('/form', express.static(__dirname + '/index.html'));

var permitedFiles = ["jpeg", "jpg", "png", "webp", "mp3"]

// default options
app.use(fileUpload());

app.get('/ping', function (req, res) {
  res.send('pong');
});

app.post('/upload', async function (req, res) {
  let multFiles = req.files.multFiles;
  let uploadPath;
  let list = [];

  if (!req.files || Object.keys(req.files).length === 0) {
    res.status(400).send('No files were uploaded.');
    return;
  }

  if (multFiles) {
    if (!Array.isArray(multFiles)) multFiles = [multFiles];
    if (!checkFileTypes(multFiles)) return res.status(500).send("Unsupported file type detected.");
    for (const file of multFiles) {
      uploadPath = __dirname + '/uploads/' + file.name;
      list.push('/uploads/' + file.name);
      file.mv(uploadPath, function (err) {
        if (err) return res.status(500).send("Error uploading files.");
      });
    }
    res.send(list);
  }
});

function checkFileTypes(multFiles) {
  for (const file of multFiles) {
    if (file.name.split(".").length === 1) return false;
    if (!permitedFiles.includes(file.name.split(".").pop())) return false;
  }
  return true;
}

app.listen(PORT, function () {
  console.log('Express server listening on port ', PORT); // eslint-disable-line
});
