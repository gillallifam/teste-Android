const express = require('express');
const fileUpload = require('../lib/index');
const app = express();

const PORT = 8000;
app.use('/form', express.static(__dirname + '/index.html'));

// default options
app.use(fileUpload());

app.get('/ping', function (req, res) {
  res.send('pong');
});

app.post('/upload', async function (req, res) {
  let uploadPath;
  let list = [];

  if (!req.files || Object.keys(req.files).length === 0) {
    res.status(400).send('No files were uploaded.');
    return;
  }

  for (const file of req.files.multFiles) {
    if (file.name.split(".").pop() === "exe") {
      return res.status(500).send("Unsupported file type.");
    }
    uploadPath = __dirname + '/uploads/' + file.name;
    list.push('/uploads/' + file.name);
    file.mv(uploadPath, function (err) {
      if (err) {
        return res.status(500).send("Error uploading files.");
      }
    });
  }
  res.send(list);
});

app.listen(PORT, function () {
  console.log('Express server listening on port ', PORT); // eslint-disable-line
});
