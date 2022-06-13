$('.dropdown-item').click(function (){
    $('#dropdownMenuButton1').text($(this).text());

    let chartStatus = Chart.getChart("myChart"); // <canvas> id
    if (chartStatus !== undefined) {
        chartStatus.destroy();
    }

    var currency = $(this).text().split('(')[1].split(')')[0];

    var settings = {
        "url": "./"+currency,
        "method": "GET",
        "timeout": 0,
    };

    $.ajax(settings).done(function (response, status, xhr){
        if(xhr.status !== 200){
            alert("error");
        }else{
            console.log(response);
            var jsonRes = JSON.parse(response);
            var buy = jsonRes.Line.series[0].data;
            var buyPriceArray = [];
            var timeArray = [];
            for(var i = 0;i < buy.length;i++){
                var time = buy[i][0];
                var price = buy[i][1];
                timeArray.push(new Date(time).toLocaleDateString());
                buyPriceArray.push(price);
            }

            var sell = jsonRes.Line.series[1].data;
            var sellPriceArray = [];
            for(var i = 0;i < buy.length;i++){
                var price = sell[i][1];
                sellPriceArray.push(price);
            }

            var ctx = document.getElementById('myChart').getContext('2d');
            var myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: timeArray,
                    datasets: [
                        {
                            label: 'Buy',
                            data: buyPriceArray,
                            borderColor:"rgba(255,99,132,0.5)",
                            backgroundColor: "rgba(255,99,132,0.2)",
                        },
                        {
                            label: 'Sell',
                            data: sellPriceArray,
                            borderColor:"rgba(0,255,132,0.5)",
                            backgroundColor: "rgba(0,255,132,0.2)",
                        }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            min: Math.min(sellPriceArray),
                            max: Math.max(buyPriceArray),
                        }
                    }
                }
            });
        }
    });
})