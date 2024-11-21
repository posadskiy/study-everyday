In the context of **load balancing**, particularly in **Kubernetes**, **Ingress** is a resource that manages external
access to services within a Kubernetes cluster. It acts as a Layer 7 (HTTP/HTTPS) load balancer, routing traffic to
appropriate services based on rules like hostnames, paths, or protocols.

---

### **How Ingress Works**

1. **Ingress Resource**:
    - A Kubernetes API object that defines routing rules for incoming HTTP/HTTPS traffic.
    - Specifies how traffic should be distributed to services within the cluster based on conditions like:
        - URL paths (`/api`, `/frontend`)
        - Hostnames (`api.example.com`, `frontend.example.com`)

2. **Ingress Controller**:
    - A Kubernetes component that implements the actual load balancing functionality.
    - Responsible for monitoring the Ingress resources and configuring the underlying load balancer (e.g., NGINX,
      Traefik) to apply the routing rules.

3. **Underlying Load Balancer**:
    - Depending on the cloud provider or configuration, Ingress controllers often leverage external load balancers (like
      AWS ALB or GCP Load Balancer) or software solutions like NGINX or HAProxy.

---

### **Why Use Ingress?**

Without Ingress, you'd typically expose services using Kubernetes **Services** of type:

- **NodePort**: Maps an external port to a port on the node for each service.
- **LoadBalancer**: Creates a dedicated cloud-based load balancer for each service.

With Ingress, you can:

- Consolidate multiple services under a single IP address.
- Simplify routing and TLS termination.
- Reduce the overhead of creating separate load balancers for every service.

---

### **Components of Ingress**

1. **Rules**:
    - Define how traffic is routed to services.
   ```yaml
   rules:
     - host: example.com
       http:
         paths:
           - path: /api
             backend:
               service:
                 name: api-service
                 port:
                   number: 80
   ```

2. **TLS**:
    - Manage SSL termination directly at the Ingress level.
   ```yaml
   tls:
     - hosts:
         - example.com
       secretName: example-tls
   ```

3. **Backend Services**:
    - Services inside the cluster that receive traffic routed by Ingress.

---

### **Common Ingress Controllers**

These controllers implement the Ingress API and handle traffic routing:

1. **NGINX Ingress Controller**:
    - The most widely used, offering robust features and customizability.
    - Handles HTTP/HTTPS traffic with high performance.

2. **Traefik**:
    - Easy-to-use and designed for dynamic environments with service discovery.
    - Supports advanced features like metrics and rate-limiting.

3. **AWS ALB Ingress Controller**:
    - Directly integrates with AWS's Application Load Balancer (ALB).

4. **GCP Ingress Controller**:
    - Leverages Google Cloud Load Balancer for global load balancing.

5. **Istio Gateway**:
    - Part of the Istio service mesh, providing more advanced traffic control.

---

### **Example Use Case**

Imagine you have two services, `api-service` and `frontend-service`. You want to:

- Route `/api` to `api-service`.
- Route `/frontend` to `frontend-service`.
- Use `example.com` as the domain.

You can create an Ingress resource like this:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: example-ingress
spec:
  rules:
    - host: example.com
      http:
        paths:
          - path: /api
            pathType: Prefix
            backend:
              service:
                name: api-service
                port:
                  number: 80
          - path: /frontend
            pathType: Prefix
            backend:
              service:
                name: frontend-service
                port:
                  number: 80
  tls:
    - hosts:
        - example.com
      secretName: tls-secret
```

---

### **Key Benefits of Ingress**

1. Centralized management of HTTP/HTTPS routing.
2. Consolidation of multiple services under a single IP.
3. Native support for features like path-based routing, TLS termination, and host-based routing.
