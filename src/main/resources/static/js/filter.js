function filter() {
          // Declare variables
          var input, filter, table, tr, td, i, txtValue;
          input = document.getElementById("filterText");
          filter = input.value.toUpperCase();
          table = document.getElementById("myTable");
          tr = table.getElementsByTagName("tr");
          var searchType =  document.getElementById("searchType").value;

          // Loop through all table rows, and hide those who don't match the search query
          for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[searchType];
                if (td) {
                  txtValue = td.textContent || td.innerText;
                  if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = "";
                  } else {
                    tr[i].style.display = "none";
                  }
                }
          }
        }