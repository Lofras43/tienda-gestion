let productosVenta = [];
let totalVenta = 0;

function actualizarPrecio() {
    const select = document.getElementById('producto');
    const precioInput = document.getElementById('precio');
    const cantidadInput = document.getElementById('cantidad');
    
    const option = select.options[select.selectedIndex];
    if (option && option.value) {
        const precio = option.getAttribute('data-precio');
        precioInput.value = precio;
        calcularTotal();
    } else {
        precioInput.value = '';
    }
}

function calcularTotal() {
    const cantidad = parseInt(document.getElementById('cantidad').value) || 0;
    const precio = parseFloat(document.getElementById('precio').value) || 0;
    const total = cantidad * precio;
    document.getElementById('total').value = total.toFixed(2);
}

function agregarProducto() {
    const select = document.getElementById('producto');
    const cantidad = parseInt(document.getElementById('cantidad').value) || 0;
    const precio = parseFloat(document.getElementById('precio').value) || 0;
    
    if (!select.value) {
        alert('Por favor selecciona un producto');
        return;
    }
    
    if (cantidad <= 0) {
        alert('La cantidad debe ser mayor a 0');
        return;
    }
    
    const option = select.options[select.selectedIndex];
    const nombreProducto = option.text.split(' - ')[0];
    const stockDisponible = parseInt(option.getAttribute('data-stock'));
    
    const productoExistente = productosVenta.find(p => p.id === select.value);
    const cantidadActual = productoExistente ? productoExistente.cantidad : 0;
    
    if (cantidadActual + cantidad > stockDisponible) {
        alert('Stock insuficiente. Stock disponible: ' + stockDisponible);
        return;
    }
    
    if (productoExistente) {
        productoExistente.cantidad += cantidad;
        productoExistente.subtotal = productoExistente.cantidad * precio;
    } else {
        productosVenta.push({
            id: select.value,
            nombre: nombreProducto,
            cantidad: cantidad,
            precio: precio,
            subtotal: cantidad * precio
        });
    }
    
    actualizarTabla();
    
    select.value = '';
    document.getElementById('cantidad').value = 1;
    document.getElementById('precio').value = '';
    document.getElementById('total').value = '0';
}

function actualizarTabla() {
    const tbody = document.getElementById('tabla-productos');
    tbody.innerHTML = '';
    totalVenta = 0;
    
    productosVenta.forEach((producto, index) => {
        totalVenta += producto.subtotal;
        
        const row = `
            <tr>
                <td>${producto.nombre}</td>
                <td>${producto.cantidad}</td>
                <td>S/ ${producto.precio.toFixed(2)}</td>
                <td>S/ ${producto.subtotal.toFixed(2)}</td>
                <td>
                    <button onclick="eliminarProducto(${index})" class="btn btn-small btn-danger">Eliminar</button>
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
    
    document.getElementById('total-venta').innerHTML = '<strong>S/ ' + totalVenta.toFixed(2) + '</strong>';
}

function eliminarProducto(index) {
    productosVenta.splice(index, 1);
    actualizarTabla();
}

function registrarVenta() {
    if (productosVenta.length === 0) {
        alert('Agrega al menos un producto a la venta');
        return;
    }
    
    const productoIds = [];
    const cantidades = [];
    const precios = [];
    
    productosVenta.forEach(p => {
        productoIds.push(p.id);
        cantidades.push(p.cantidad);
        precios.push(p.precio);
    });
    
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/ventas/guardar';
    
    productoIds.forEach((id, i) => {
        const inputId = document.createElement('input');
        inputId.type = 'hidden';
        inputId.name = 'productoIds';
        inputId.value = id;
        form.appendChild(inputId);
        
        const inputCant = document.createElement('input');
        inputCant.type = 'hidden';
        inputCant.name = 'cantidades';
        inputCant.value = cantidades[i];
        form.appendChild(inputCant);
        
        const inputPrecio = document.createElement('input');
        inputPrecio.type = 'hidden';
        inputPrecio.name = 'precios';
        inputPrecio.value = precios[i];
        form.appendChild(inputPrecio);
    });
    
    document.body.appendChild(form);
    form.submit();
}