<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/multiple-select.min.css">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="${ctx!}/css/GA.css" rel="stylesheet">

    <!--The JS File used to deal csv.-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/PapaParse/5.3.0/papaparse.min.js"></script>

</head>
<body>
<!-- 全局js -->
<script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/js/xlsx.full.min.js"></script>
<script src="${ctx!}/js/plugins/chosen/chosen.jquery.js"></script>
<script src="${ctx!}/js/bootstrap.min.js"></script>
<script src="${ctx!}/js/echarts.min.js"></script>

<!-- Bootstrap table -->
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

<!-- Peity -->
<script src="${ctx!}/js/plugins/peity/jquery.peity.min.js"></script>

<script src="${ctx!}/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/js/multiple-select.min.js"></script>


<!-- 自定义js -->
<script src="${ctx!}/js/content.js?v=1.0.0"></script>
<script type="text/javascript">
    window.onload = function () {
        // 提交
        document.getElementById("l_diversity_fileUpload").addEventListener("change", choose_file)
    }
    let choose_file = function (event){
        // 清空
        document.getElementById("fileInfo").innerHTML = "";
        document.getElementById("after").innerHTML = "";

        //读取文件
        const file = event.target.files[0]
        // 文件名，扩展名
        const fileName = file.name;
        const fileExtension = fileName.split('.').pop().toLowerCase();
        if (file) {
            if ("csv" === fileExtension) {
                let fileLoad = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                    "<strong>" + fileName + "文件</strong>上传成功"
                "</span>" +
                "</div>";
                document.getElementById("fileInfo").innerHTML = fileLoad
                //console.log(fileExtension)
                //构建formData,发送给后端
                const formData = new FormData();
                formData.set("file", file);
                formData.set("sheet", "l_diversity");
                formData.set("algName", "l_diversity");
                formData.set("params", document.getElementById("l_diversity_privacyLevel").value);
                document.getElementById("l_diversity_submit").onclick = function (){
                    fetch('/File/desenFile', {
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.blob())
                        .then(blob => {
                            // 脱敏前
                            let reader = new FileReader();
                            reader.onload = function (e) {
                                let data = new Uint8Array(e.target.result);
                                let workbook = XLSX.read(data, { type: 'array' });

                                let sheetName = workbook.SheetNames[0];
                                let sheet = workbook.Sheets[sheetName];

                                let jsonData = XLSX.utils.sheet_to_json(sheet, { header: 1 });

                                let pageSize = 10;
                                let pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                                let currentPage = 1;

                                function displayTable(page) {
                                    let startIndex = (page - 1) * pageSize + 1; // 跳过表头
                                    let endIndex = Math.min(startIndex + pageSize, jsonData.length);

                                    let tableContent = '<thead><tr>';
                                    let headers = ['age', 'work_class', 'fin_weight', 'education', 'edu_num', 'mar_status', 'occupation', 'relaship',
                                        'race', 'gender', 'cap_gain', 'cap_loss', 'hours_pweek', 'country', 'income'];
                                    headers.forEach(function (header) {
                                        tableContent += '<th style=\"white-space: nowrap;\">' + header + '</th>';
                                    });
                                    tableContent += '</tr></thead><tbody>';

                                    for (let i = startIndex; i < endIndex; i++) {
                                        tableContent += '<tr>';
                                        for (let j = 0; j < headers.length; j++) {
                                            let cellValue = (jsonData[i][j] !== undefined) ? jsonData[i][j] : '';
                                            tableContent += '<td>' + cellValue + '</td>';
                                        }
                                        tableContent += '</tr>';
                                    }

                                    tableContent += '</tbody>';

                                    $('#dataTable').html(tableContent);
                                }

                                displayTable(currentPage);

                                function renderPagination() {
                                    let pagination = '<li class="page-item"><a class="page-link" href="#" data-page="prev">Prev</a></li>';
                                    pagination += '<li class="page-item"><a class="page-link" href="#" data-page="next">Next</a></li>';

                                    $('#pagination').html(pagination);

                                    $('#pagination a').click(function (e) {
                                        e.preventDefault();
                                        let page = $(this).data('page');
                                        console.log(page)
                                        if (page === 'prev') {
                                            currentPage = Math.max(1, currentPage - 1);
                                        } else if (page === 'next') {
                                            currentPage = Math.min(pageCount, currentPage + 1);
                                        }
                                        displayTable(currentPage);
                                        renderPagination();
                                    });

                                    $('#totalPages').text(pageCount);
                                }

                                $('#paginationContainer').show();
                                renderPagination();

                                $('#goToPage').click(function () {
                                    let pageNumber = parseInt($('#pageInput').val());
                                    if (pageNumber >= 1 && pageNumber <= pageCount) {
                                        currentPage = pageNumber;
                                        displayTable(currentPage);
                                        renderPagination();
                                    } else {
                                        alert('请输入有效页数！');
                                    }
                                });
                            };
                            reader.readAsArrayBuffer(file);

                            // 脱敏后
                            const reader1 = new FileReader();
                            reader1.onload = function(event) {
                                const data = event.target.result;
                                const workbook = XLSX.read(data, { type: 'binary' });
                                const sheetName = workbook.SheetNames[0];
                                const sheet = workbook.Sheets[sheetName];
                                const jsonData = XLSX.utils.sheet_to_json(sheet, { header: 1 });
                                let pageSize = 10;
                                let pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                                let currentPage1 = 1;

                                function displayTable1(page1) {
                                    let startIndex1 = (page1 - 1) * pageSize + 1; // 跳过表头
                                    let endIndex = Math.min(startIndex1 + pageSize, jsonData.length);

                                    let tableContent1 = '<thead><tr>';
                                    let headers1 = jsonData[0];
                                    headers1.forEach(function (header1) {
                                        tableContent1 += '<th style=\"white-space: nowrap;\">' + header1 + '</th>';
                                    });
                                    tableContent1 += '</tr></thead><tbody>';

                                    for (let i = startIndex1; i < endIndex; i++) {
                                        tableContent1 += '<tr>';
                                        for (let j = 0; j < headers1.length; j++) {
                                            let cellValue = (jsonData[i][j] !== undefined) ? jsonData[i][j] : '';
                                            tableContent1 += '<td>' + cellValue + '</td>';
                                        }
                                        tableContent1 += '</tr>';
                                    }

                                    tableContent1 += '</tbody>';

                                    $('#dataTable1').html(tableContent1);
                                }

                                displayTable1(currentPage1);

                                function renderPagination1() {
                                    let pagination1 = '<li class="page-item"><a class="page-link" href="#" data-page="prev1">Prev</a></li>';
                                    pagination1 += '<li class="page-item"><a class="page-link" href="#" data-page="next1">Next</a></li>';

                                    $('#pagination1').html(pagination1);

                                    $('#pagination1 a').click(function (e) {
                                        e.preventDefault();
                                        let page = $(this).data('page');
                                        console.log(page)
                                        if (page === 'prev1') {
                                            currentPage1 = Math.max(1, currentPage1 - 1);
                                        } else if (page === 'next1') {
                                            currentPage1 = Math.min(pageCount, currentPage1 + 1);
                                        }
                                        displayTable1(currentPage1);
                                        renderPagination1();
                                    });

                                    $('#totalPages1').text(pageCount);
                                }

                                $('#paginationContainer1').show();
                                renderPagination1();

                                $('#goToPage1').click(function () {
                                    let pageNumber1 = parseInt($('#pageInput1').val());
                                    if (pageNumber1 >= 1 && pageNumber1 <= pageCount) {
                                        currentPage1 = pageNumber1;
                                        displayTable1(currentPage1);
                                        renderPagination1();
                                    } else {
                                        alert('请输入有效页数！');
                                    }
                                });
                            };

                            reader1.readAsBinaryString(blob);

                            // 创建一个下载链接
                            const downloadLink = document.createElement('a');
                            downloadLink.href = URL.createObjectURL(blob);
                            downloadLink.download = Date.now().toString() + ".csv"; // 下载的文件名
                            downloadLink.click();
                            let after = document.getElementById("after");
                            after.appendChild(downloadLink);
                        })
                        .catch(error => console.error('Error:', error));
                }
            }
            else{
                alert("请提交csv文件")
            }
        }

    }

</script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('l_diversity_entropy_fileUpload').addEventListener('change', handleFileSelect, {passive: false});
        document.getElementById('l_diversity_entropy_submit').addEventListener('click', handleSubmit);
        document.getElementById('prevPage').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentPage > 1) {
                currentPage--;
                displayTablePage(currentPage);
                updatePagination();
            }
        });
        document.getElementById('desensitizedPrevPage').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentDesensitizedPage > 1) {
                currentDesensitizedPage--;
                displayDesensitizedTablePage(currentDesensitizedPage);
                updateDesensitizedPagination();
            }
        });
        document.getElementById('nextPage').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentPage < PageCount) {
                currentPage++;
                displayTablePage(currentPage);
                updatePagination();
            }
        });
        document.getElementById('desensitizedNextPage').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentDesensitizedPage < desensitizedPageCount) {
                currentDesensitizedPage++;
                displayDesensitizedTablePage(currentDesensitizedPage);
                updateDesensitizedPagination();
            }
        });
        document.getElementById('pageInput').addEventListener('input', function(event) {
            const page = parseInt(event.target.value);
            if (!isNaN(page) && page >= 1 && page <= PageCount) {
                currentPage = page;
                displayTablePage(page);
                updatePagination();
            }
        });
        document.getElementById('desensitizedPageInput').addEventListener('input', function(event) {
            const page = parseInt(event.target.value);
            if (!isNaN(page) && page >= 1 && page <= desensitizedPageCount) {
                currentDesensitizedPage = page;
                displayDesensitizedTablePage(page);
                updateDesensitizedPagination();
            }
        });
    });

    const rowsPerPage = 10;

    let currentPage = 1;
    let csvData = [];
    let attributes = [];
    let PageCount = 1;

    let currentDesensitizedPage = 1;
    let desensitizedData = [];
    let desensitizedPageCount = 1;

    function handleFileSelect(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const text = e.target.result;
                processCSV(text);
            };
            reader.readAsText(file);
        }
    }

    function processCSV(csvText) {
        Papa.parse(csvText, {
            complete: function(results) {
                csvData = results.data; // Get all rows including header
                PageCount = Math.ceil((csvData.length - 1) / rowsPerPage); // Exclude header row
                displayTablePage(1);
                updatePagination();
                displayAttributes(csvData[0]);
            },
            header: false
        });
    }

    function parseDesensitizedCSV(csvText) {
        Papa.parse(csvText, {
            complete: function(results) {
                desensitizedData = results.data;
                desensitizedPageCount = Math.ceil((desensitizedData.length - 1) / rowsPerPage);
                displayDesensitizedTablePage(1);
                updateDesensitizedPagination();
            },
            header: false
        });
    }

    function updatePagination() {
        const pageInput = document.getElementById('pageInputEntropy');
        pageInput.value = currentPage;
        pageInput.max = PageCount;
    }

    function updateDesensitizedPagination() {
        const pageInput = document.getElementById('desensitizedPageInput');
        pageInput.value = currentDesensitizedPage;
        pageInput.max = desensitizedPageCount;
    }

    function displayTablePage(page) {
        document.getElementById('paginationContainerInput').style.display = 'flex';
        const tableBody = document.getElementById('tableBody');
        tableBody.innerHTML = ''; // Clear existing rows

        const start = (page - 1) * rowsPerPage + 1; // Skip header row
        const end = start + rowsPerPage;
        const paginatedData = csvData.slice(start, end);

        paginatedData.forEach(row => {
            const tr = document.createElement('tr');
            row.forEach(cell => {
                const td = document.createElement('td');
                td.textContent = cell;
                tr.appendChild(td);
            });
            tableBody.appendChild(tr);
        });

        // Display headers
        const tableHeader = document.getElementById('tableHeader');
        tableHeader.innerHTML = ''; // Clear existing headers
        csvData[0].forEach(header => {
            const th = document.createElement('th');
            th.textContent = header;
            tableHeader.appendChild(th);
        });
    }

    function displayDesensitizedTablePage(page) {
        const tableBody = document.getElementById('desensitizedTableBody');
        tableBody.innerHTML = '';

        const start = (page - 1) * rowsPerPage + 1;
        const end = start + rowsPerPage;
        const paginatedData = desensitizedData.slice(start, end);

        if (paginatedData.length > 0) {
            paginatedData.forEach(row => {
                const tr = document.createElement('tr');
                row.forEach(cell => {
                    const td = document.createElement('td');
                    td.textContent = cell;
                    td.classList.add('fixed-width');
                    tr.appendChild(td);
                });
                tableBody.appendChild(tr);
            });

            // Display headers
            const tableHeader = document.getElementById('desensitizedTableHeader');
            tableHeader.innerHTML = '';
            desensitizedData[0].forEach(header => {
                const th = document.createElement('th');
                th.textContent = header;
                th.classList.add('fixed-width');
                tableHeader.appendChild(th);
            });
        }

    }

    function displayAttributes(attributes) {
        const tableBody = document.getElementById('attributesTable').querySelector('tbody');
        tableBody.innerHTML = ''; // Clear existing rows

        attributes.forEach(attribute => {
            const row = document.createElement('tr');

            const attributeCell = document.createElement('td');
            attributeCell.textContent = attribute;
            attributeCell.classList.add('fixed-width'); // Fixed width for attribute cells
            row.appendChild(attributeCell);

            const templateCell = document.createElement('td');
            templateCell.classList.add('fixed-width'); // Fixed width for template cells
            const templateInput = document.createElement('input');
            templateInput.type = 'file';
            templateInput.accept = '.csv'; // Assuming templates are in CSV format
            templateInput.name = attribute; // Set the name of the input to the attribute
            templateCell.appendChild(templateInput);
            row.appendChild(templateCell);

            const sensitiveCell = document.createElement('td');
            sensitiveCell.classList.add('fixed-width'); // Fixed width for sensitive attribute cells
            const sensitiveInput = document.createElement('input');
            sensitiveInput.type = 'radio';
            sensitiveInput.name = 'sensitive_attribute';
            sensitiveInput.value = attribute;
            sensitiveCell.appendChild(sensitiveInput);
            row.appendChild(sensitiveCell);

            tableBody.appendChild(row);
        });
    }



    function handleSubmit(event) {
        event.preventDefault(); // Call preventDefault if needed
        const tableBody = document.getElementById('attributesTable').querySelector('tbody');
        const rows = tableBody.querySelectorAll('tr');
        const formData = new FormData();

        rows.forEach(row => {
            const attribute = row.querySelector('td').textContent;
            const fileInput = row.querySelector('input[type="file"]');
            const file = fileInput.files[0];
            if (file) {
                formData.append(attribute, file);
            }
        });

        const csvFileInput = document.getElementById('l_diversity_entropy_fileUpload');
        const csvFile = csvFileInput.files[0];
        if (csvFile) {
            formData.append('csvFile', csvFile);
        }

        formData.append("params", document.getElementById("l_diversity_entropy_privacyLevel").value);

        const attribute = document.querySelector('input[name="sensitive_attribute"]:checked');
        formData.append('attribute', attribute.value);

        // Replace 'YOUR_SERVER_ENDPOINT' with your actual server endpoint
        fetch('/KAnonymity/LDiversity/Entropy', {
            method: 'POST',
            body: formData
        }).then(response => response.blob()).then(blob => {
            parseDesensitizedCSV(blob);
            displayDesensitizedTablePage(1);
            document.getElementById('paginationContainerOutput').style.display = 'flex';
            const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.style.display = 'none';
                a.href = url;
                a.download = "output_" + csvFile.name;
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
            }).catch(error => {
                console.error('Error:', error);
            });
    }
</script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('l_diversity_RecursiveC_fileUpload').addEventListener('change', handleFileSelectOfRecursiveC, {passive: false});
        document.getElementById('l_diversity_RecursiveC_submit').addEventListener('click', handleSubmitOfRecursiveC);
        document.getElementById('prevPageOfRecursiveCInput').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentPageOfRecursiveC > 1) {
                currentPageOfRecursiveC--;
                displayTablePageOfRecursiveC(currentPageOfRecursiveC);
                updatePaginationOfRecursiveC();
            }
        });
        document.getElementById('desensitizedPrevPageOfRecursiveCOutput').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentDesensitizedPageOfRecursiveC > 1) {
                currentDesensitizedPageOfRecursiveC--;
                displayDesensitizedTablePageOfRecursiveC(currentDesensitizedPageOfRecursiveC);
                updateDesensitizedPaginationOfRecursiveC();
            }
        });
        document.getElementById('nextPageOfRecursiveCInput').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentPageOfRecursiveC < PageCountOfRecursiveC) {
                currentPageOfRecursiveC++;
                displayTablePageOfRecursiveC(currentPageOfRecursiveC);
                updatePaginationOfRecursiveC();
            }
        });
        document.getElementById('desensitizedNextPageOfRecursiveCOutput').addEventListener('click', function(event) {
            event.preventDefault();
            if (currentDesensitizedPageOfRecursiveC < desensitizedPageCountOfRecursiveC) {
                currentDesensitizedPageOfRecursiveC++;
                displayDesensitizedTablePageOfRecursiveC(currentDesensitizedPageOfRecursiveC);
                updateDesensitizedPaginationOfRecursiveC();
            }
        });
        document.getElementById('pageInputOfRecursiveCInput').addEventListener('input', function(event) {
            const page = parseInt(event.target.value);
            if (!isNaN(page) && page >= 1 && page <= PageCountOfRecursiveC) {
                currentPageOfRecursiveC = page;
                displayTablePageOfRecursiveC(page);
                updatePaginationOfRecursiveC();
            }
        });
        document.getElementById('desensitizedPageInputOfRecursiveCOutput').addEventListener('input', function(event) {
            const page = parseInt(event.target.value);
            if (!isNaN(page) && page >= 1 && page <= desensitizedPageCount) {
                currentDesensitizedPage = page;
                displayDesensitizedTablePage(page);
                updateDesensitizedPagination();
            }
        });
    });

    const rowsPerPageOfRecursiveC = 10;

    let currentPageOfRecursiveC = 1;
    let csvDataOfRecursiveC = [];
    let attributesOfRecursiveC = [];
    let PageCountOfRecursiveC = 1;

    let currentDesensitizedPageOfRecursiveC = 1;
    let desensitizedDataOfRecursiveC = [];
    let desensitizedPageCountOfRecursiveC = 1;

    function handleFileSelectOfRecursiveC(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const text = e.target.result;
                processCSVOfRecursiveC(text);
            };
            reader.readAsText(file);
        }
    }

    function processCSVOfRecursiveC(csvText) {
        Papa.parse(csvText, {
            complete: function(results) {
                csvDataOfRecursiveC = results.data; // Get all rows including header
                PageCountOfRecursiveC = Math.ceil((csvDataOfRecursiveC.length - 1) / rowsPerPageOfRecursiveC); // Exclude header row
                displayTablePageOfRecursiveC(1);
                updatePaginationOfRecursiveC();
                displayAttributesOfRecursiveC(csvDataOfRecursiveC[0]);
            },
            header: false
        });
    }

    function parseDesensitizedCSVOfRecursiveC(csvText) {
        Papa.parse(csvText, {
            complete: function(results) {
                desensitizedDataOfRecursiveC = results.data;
                desensitizedPageCountOfRecursiveC = Math.ceil((desensitizedDataOfRecursiveC.length - 1) / rowsPerPageOfRecursiveC);
                displayDesensitizedTablePageOfRecursiveC(1);
                updateDesensitizedPaginationOfRecursiveC();
            },
            header: false
        });
    }

    function updatePaginationOfRecursiveC() {
        const pageInput = document.getElementById('pageInputOfRecursiveCInput');
        pageInput.value = currentPageOfRecursiveC;
        pageInput.max = PageCountOfRecursiveC;
    }

    function updateDesensitizedPaginationOfRecursiveC() {
        const pageInput = document.getElementById('desensitizedPageInputOfRecursiveCOutput');
        pageInput.value = currentDesensitizedPageOfRecursiveC;
        pageInput.max = desensitizedPageCountOfRecursiveC;
    }

    function displayTablePageOfRecursiveC(page) {
        document.getElementById('paginationContainerOfRecursiveCInput').style.display = 'flex';
        const tableBody = document.getElementById('tableBodyOfRecursiveCInput');
        tableBody.innerHTML = ''; // Clear existing rows

        const start = (page - 1) * rowsPerPageOfRecursiveC + 1; // Skip header row
        const end = start + rowsPerPageOfRecursiveC;
        const paginatedData = csvDataOfRecursiveC.slice(start, end);

        paginatedData.forEach(row => {
            const tr = document.createElement('tr');
            row.forEach(cell => {
                const td = document.createElement('td');
                td.textContent = cell;
                tr.appendChild(td);
            });
            tableBody.appendChild(tr);
        });

        // Display headers
        const tableHeader = document.getElementById('tableHeaderOfRecursiveCInput');
        tableHeader.innerHTML = ''; // Clear existing headers
        csvDataOfRecursiveC[0].forEach(header => {
            const th = document.createElement('th');
            th.textContent = header;
            tableHeader.appendChild(th);
        });
    }

    function displayDesensitizedTablePageOfRecursiveC(page) {
        const tableBody = document.getElementById('desensitizedTableBodyOfRecursiveCOutput');
        tableBody.innerHTML = '';

        const start = (page - 1) * rowsPerPageOfRecursiveC + 1;
        const end = start + rowsPerPageOfRecursiveC;
        const paginatedData = desensitizedDataOfRecursiveC.slice(start, end);

        if (paginatedData.length > 0) {
            paginatedData.forEach(row => {
                const tr = document.createElement('tr');
                row.forEach(cell => {
                    const td = document.createElement('td');
                    td.textContent = cell;
                    td.classList.add('fixed-width');
                    tr.appendChild(td);
                });
                tableBody.appendChild(tr);
            });

            // Display headers
            const tableHeader = document.getElementById('desensitizedTableHeaderOfRecursiveCOutput');
            tableHeader.innerHTML = '';
            desensitizedDataOfRecursiveC[0].forEach(header => {
                const th = document.createElement('th');
                th.textContent = header;
                th.classList.add('fixed-width');
                tableHeader.appendChild(th);
            });
        }

    }

    function displayAttributesOfRecursiveC(attributes) {
        const tableBody = document.getElementById('attributesTableOfRecursiveCInput').querySelector('tbody');
        tableBody.innerHTML = ''; // Clear existing rows

        attributes.forEach(attribute => {
            const row = document.createElement('tr');

            const attributeCell = document.createElement('td');
            attributeCell.textContent = attribute;
            attributeCell.classList.add('fixed-width'); // Fixed width for attribute cells
            row.appendChild(attributeCell);

            const templateCell = document.createElement('td');
            templateCell.classList.add('fixed-width'); // Fixed width for template cells
            const templateInput = document.createElement('input');
            templateInput.type = 'file';
            templateInput.accept = '.csv'; // Assuming templates are in CSV format
            templateInput.name = attribute; // Set the name of the input to the attribute
            templateCell.appendChild(templateInput);
            row.appendChild(templateCell);

            const sensitiveCell = document.createElement('td');
            sensitiveCell.classList.add('fixed-width'); // Fixed width for sensitive attribute cells
            const sensitiveInput = document.createElement('input');
            sensitiveInput.type = 'radio';
            sensitiveInput.name = 'sensitive_attribute_c';
            sensitiveInput.value = attribute;
            sensitiveCell.appendChild(sensitiveInput);
            row.appendChild(sensitiveCell);

            tableBody.appendChild(row);
        });
    }



    function handleSubmitOfRecursiveC(event) {
        event.preventDefault(); // Call preventDefault if needed
        const tableBody = document.getElementById('attributesTableOfRecursiveCInput').querySelector('tbody');
        const rows = tableBody.querySelectorAll('tr');
        const formData = new FormData();

        rows.forEach(row => {
            const attribute = row.querySelector('td').textContent;
            const fileInput = row.querySelector('input[type="file"]');
            const file = fileInput.files[0];
            if (file) {
                formData.append(attribute, file);
            }
        });

        const csvFileInput = document.getElementById('l_diversity_RecursiveC_fileUpload');
        const csvFile = csvFileInput.files[0];
        if (csvFile) {
            formData.append('csvFile', csvFile);
        }

        formData.append("params", document.getElementById("l_diversity_RecursiveC_privacyLevel").value);

        const attribute = document.querySelector('input[name="sensitive_attribute_c"]:checked');
        formData.append('attribute', attribute.value);

        // Replace 'YOUR_SERVER_ENDPOINT' with your actual server endpoint
        fetch('/KAnonymity/LDiversity/RecursiveC', {
            method: 'POST',
            body: formData
        }).then(response => response.blob()).then(blob => {
            parseDesensitizedCSVOfRecursiveC(blob);
            displayDesensitizedTablePageOfRecursiveC(1);
            document.getElementById('paginationContainerOfRecursiveCOutput').style.display = 'flex';
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = "output_" + csvFile.name;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        }).catch(error => {
            console.error('Error:', error);
        });
    }
</script>

<div class="panel panel-default">
<#--    <div class="panel-heading"  style="text-align: center;">-->
<#--        <h1 class="panel-title"><b style="font-size: 2em">l-多样性</b></h1>-->
<#--    </div>-->
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">7. l-多样性</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对csv文件进行l-多样性处理
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="<#--col-sm-5 m-b-xs d-flex--> align-items-center">
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="l_diversity_fileUpload"  style="display: none;">
                                <label for="l_diversity_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <!--文件上传信息-->
                    <div id = "fileInfo">
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="l_diversity_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="l_diversity_submit"> 提交脱敏</button>
        </div>
        <div id = "after">

        </div>

    </div>
    <div class="container mt-5">
        <div id="dataTableContainer">
            <table id="dataTable" class="table table-bordered">
                <!-- 这里将用 JavaScript 动态创建表格内容 -->
            </table>
        </div>
        <div id="paginationContainer" class="mt-3" style="display: none;">
            <nav>
                <div id="paginationInfo" class="d-flex justify-content-between align-items-center">
                    <ul class="pagination mb-0" id="pagination"></ul>
                    <div class="form-group mb-0 text-center">
                        <label for="pageInput">跳转至：</label>
                        <input type="number" class="form-control" id="pageInput" min="1">
                        <button class="btn btn-primary mt-2" id="goToPage">跳转</button>
                    </div>
                    <div id="totalPages"></div>
                </div>
            </nav>
        </div>
        <div id="dataTableContainer1">
            <table id="dataTable1" class="table table-bordered">
                <!-- 这里将用 JavaScript 动态创建表格内容 -->
            </table>
        </div>
        <div id="paginationContainer1" class="mt-3" style="display: none;">
            <nav>
                <div id="paginationInfo1" class="d-flex justify-content-between align-items-center">
                    <ul class="pagination mb-0" id="pagination1"></ul>
                    <div class="form-group mb-0 text-center">
                        <label for="pageInput1">跳转至：</label>
                        <input type="number" class="form-control" id="pageInput1" min="1">
                        <button class="btn btn-primary mt-2" id="goToPage1">跳转</button>
                    </div>
                    <div id="totalPages1"></div>
                </div>
            </nav>
        </div>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">8. L-多样性 Entropy-L-diversity</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对csv文件进行l-多样性 Entropy-l-diversity 处理
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="<#--col-sm-5 m-b-xs d-flex--> align-items-center">
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="l_diversity_entropy_fileUpload"  style="display: none;">
                                <label for="l_diversity_entropy_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <!--文件上传信息-->
                    <div id = "fileInfo">
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;  margin-bottom: 20px;">
                        <div style="margin: auto; font-size: 20px" >
                            请选择隐私保护等级
                            <select id="l_diversity_entropy_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>

                    <div id="dataTableContainer">
                        <table class="table table-bordered" id="dataTable">
                            <thead>
                            <tr id="tableHeader">
                                <!-- Dynamic headers will be added here -->
                            </tr>
                            </thead>
                            <tbody id="tableBody">
                            <!-- Dynamic rows will be added here -->
                            </tbody>
                        </table>
                        <div class="pagination-container" id="paginationContainerInput">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center" id="pagination">
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Previous" id="prevPage">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                    <li class="page-item">
                                        <input type="number" id="pageInputEntropy" class="form-control" style="width: 70px; display: inline-block;" min="1">
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Next" id="nextPage">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </div>

                        <div class="table-container">
                            <table id="attributesTable" class="table table-bordered">
                                <!-- 这里将用 JavaScript 动态创建表格内容 -->
                                <thead>
                                <tr>
                                    <th class="fixed-width">属性</th>
                                    <th class="fixed-width">模板</th>
                                    <th class="fixed-width">敏感属性</th>
                                </tr>
                                </thead>
                                <tbody>
                                <!-- Dynamic rows will be added here -->
                                </tbody>
                            </table>
                        </div>
                        <div class="table-container mt-5">
                            <table class="table table-bordered" id="desensitizedTable">
                                <thead>
                                <tr id="desensitizedTableHeader">
                                    <!-- Dynamic headers will be added here -->
                                </tr>
                                </thead>
                                <tbody id="desensitizedTableBody">
                                <!-- Dynamic rows will be added here -->
                                </tbody>
                            </table>
                        </div>
                        <div class="pagination-container" id="paginationContainerOutput">
                            <nav aria-label="Page navigation">
                                <ul class="pagination" id="desensitizedPagination">
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Previous" id="desensitizedPrevPage">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                    <li class="page-item">
                                        <input type="number" id="desensitizedPageInput" class="form-control" style="width: 70px; display: inline-block;" min="1">
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Next" id="desensitizedNextPage">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>

                    <div class="btn2" style="text-align: center;">
                        <button type="button" class="btn btn-sm btn-primary" id="l_diversity_entropy_submit"> 提交脱敏</button>
                    </div>
                </div>
            </div>

        </div>

    </div>
</div>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">3 L-多样性 Recursive-C- l-diversity </p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对csv文件进行l-多样性 Recursive-C- l-diversity 处理
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="<#--col-sm-5 m-b-xs d-flex--> align-items-center">
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="l_diversity_RecursiveC_fileUpload"  style="display: none;">
                                <label for="l_diversity_RecursiveC_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <!--文件上传信息-->
                    <div id = "fileInfo">
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;  margin-bottom: 20px;">
                        <div style="margin: auto; font-size: 20px" >
                            请选择隐私保护等级
                            <select id="l_diversity_RecursiveC_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>

                    <div id="dataTableContainer">
                        <table class="table table-bordered" id="dataTableOfRecursiveCInput">
                            <thead>
                            <tr id="tableHeaderOfRecursiveCInput">
                                <!-- Dynamic headers will be added here -->
                            </tr>
                            </thead>
                            <tbody id="tableBodyOfRecursiveCInput">
                            <!-- Dynamic rows will be added here -->
                            </tbody>
                        </table>
                        <div class="pagination-container" id="paginationContainerOfRecursiveCInput">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center" id="paginationOfRecursiveCInput">
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Previous" id="prevPageOfRecursiveCInput">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                    <li class="page-item">
                                        <input type="number" id="pageInputOfRecursiveCInput" class="form-control" style="width: 70px; display: inline-block;" min="1">
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Next" id="nextPageOfRecursiveCInput">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </div>

                        <div class="table-container">
                            <table id="attributesTableOfRecursiveCInput" class="table table-bordered">
                                <!-- 这里将用 JavaScript 动态创建表格内容 -->
                                <thead>
                                <tr>
                                    <th class="fixed-width">属性</th>
                                    <th class="fixed-width">模板</th>
                                    <th class="fixed-width">敏感属性</th>
                                </tr>
                                </thead>
                                <tbody>
                                <!-- Dynamic rows will be added here -->
                                </tbody>
                            </table>
                        </div>
                        <div class="table-container mt-5">
                            <table class="table table-bordered" id="desensitizedTableOfRecursiveCOutput">
                                <thead>
                                <tr id="desensitizedTableHeaderOfRecursiveCOutput">
                                    <!-- Dynamic headers will be added here -->
                                </tr>
                                </thead>
                                <tbody id="desensitizedTableBodyOfRecursiveCOutput">
                                <!-- Dynamic rows will be added here -->
                                </tbody>
                            </table>
                        </div>
                        <div class="pagination-container" id="paginationContainerOfRecursiveCOutput">
                            <nav aria-label="Page navigation">
                                <ul class="pagination" id="desensitizedPaginationOfRecursiveCOutput">
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Previous" id="desensitizedPrevPageOfRecursiveCOutput">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                    <li class="page-item">
                                        <input type="number" id="desensitizedPageInputOfRecursiveCOutput" class="form-control" style="width: 70px; display: inline-block;" min="1">
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#" aria-label="Next" id="desensitizedNextPageOfRecursiveCOutput">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>

                    <div class="btn2" style="text-align: center;">
                        <button type="button" class="btn btn-sm btn-primary" id="l_diversity_RecursiveC_submit">提交脱敏</button>
                    </div>
                </div>
            </div>

        </div>

    </div>
</div>


</body>
<style>
    /* 设置表格样式 */
    #dataTableContainer {
        width: 100%;
        overflow-x: auto;
    }
    #dataTable {
        width: max-content;
        margin: 0 auto;
    }
    #paginationInfo {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    #paginationInfo input {
        width: 5em;
        text-align: center;
    }
    /* 设置表格样式 */
    #dataTableContainer1 {
        width: 100%;
        overflow-x: auto;
    }
    #dataTable1 {
        width: max-content;
        margin: 0 auto;
    }
    #paginationInfo1 {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    #paginationInfo1 input {
        width: 5em;
        text-align: center;
    }
    /*标题*/
    .ibox-title {
        height: 200px;
        border-color: #edf1f2;
        background-color: #dbeafe;
        color: black;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    /*选择框居中*/
    .midtile{
        line-height: 30px;
        text-align: center;
        display:flex;
        justify-content: center;
    }
    /*上传按钮*/
    .upload-btn, #l_diversity_submit, #l_diversity_entropy_submit{
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    th, td {
        padding: 8px;
        text-align: left;
    }
    .fixed-width {
        width: 200px;
    }
    .table-container {
        display: flex;
        justify-content: center;
    }
    .pagination-container {
        display: none;
        justify-content: center;
    }
</style>
</html>
