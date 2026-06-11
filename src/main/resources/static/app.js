const state = {
  catalog: { plans: [], tiers: [] }
};

const elements = {
  apiStatus: document.querySelector("#apiStatus"),
  plans: document.querySelector("#plans"),
  tiers: document.querySelector("#tiers"),
  rawResult: document.querySelector("#rawResult"),
  summary: document.querySelector("#summary"),
  subscribeForm: document.querySelector("#subscribeForm"),
  manageForm: document.querySelector("#manageForm"),
  evaluateForm: document.querySelector("#evaluateForm"),
  loadMembership: document.querySelector("#loadMembership"),
  cancelMembership: document.querySelector("#cancelMembership")
};

async function api(path, options = {}) {
  const response = await fetch(path, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options
  });
  const body = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(body.message || `Request failed with ${response.status}`);
  }
  return body;
}

function setStatus(text, ok) {
  elements.apiStatus.textContent = text;
  elements.apiStatus.className = `status ${ok ? "ok" : "error"}`;
}

function money(plan) {
  return `${plan.price.toFixed ? plan.price.toFixed(2) : plan.price}`;
}

function renderCatalog(catalog) {
  elements.plans.innerHTML = catalog.plans.map(plan => `
    <article class="plan-card">
      <strong>${plan.billingPeriod}</strong>
      <span class="price">${money(plan)}</span>
      <span class="currency">${plan.currency}</span>
    </article>
  `).join("");

  elements.tiers.innerHTML = catalog.tiers.map(tier => {
    const tierClass = tier.code.toLowerCase();
    const benefits = tier.benefits.map(benefit => `
      <span class="chip">${benefit.type.replaceAll("_", " ")}: ${benefit.value}</span>
    `).join("");
    const criteria = formatCriteria(tier.criteria);

    return `
      <article class="tier-card ${tierClass}">
        <div class="tier-top">
          <h3>${tier.displayName}</h3>
          <span class="rank">Rank ${tier.rankOrder}</span>
        </div>
        <div class="chips">${benefits}</div>
        <div class="criteria">${criteria}</div>
      </article>
    `;
  }).join("");
}

function formatCriteria(criteria) {
  if (!criteria.length) {
    return "Base tier, no eligibility requirements";
  }

  const orderCount = criteria.find(criterion => criterion.type === "MIN_MONTHLY_ORDER_COUNT");
  const orderValue = criteria.find(criterion => criterion.type === "MIN_MONTHLY_ORDER_VALUE");

  if (orderCount && orderValue && criteria.length === 2) {
    return `Requires ${orderCount.value}+ monthly orders and Rs. ${Number(orderValue.value).toLocaleString("en-IN")}+ monthly order value`;
  }

  return `Requires ${criteria.map(formatCriterion).join(" and ")}`;
}

function formatCriterion(criterion) {
  if (criterion.type === "MIN_MONTHLY_ORDER_COUNT") {
    return `${criterion.value}+ monthly orders`;
  }
  if (criterion.type === "MIN_MONTHLY_ORDER_VALUE") {
    return `Rs. ${Number(criterion.value).toLocaleString("en-IN")}+ monthly order value`;
  }
  return `${criterion.type.replaceAll("_", " ").toLowerCase()} ${criterion.value}`;
}

function fillSelects(catalog) {
  document.querySelectorAll("select[name='billingPeriod']").forEach(select => {
    select.innerHTML = catalog.plans
      .map(plan => `<option value="${plan.billingPeriod}">${plan.billingPeriod} - ${money(plan)} ${plan.currency}</option>`)
      .join("");
  });

  document.querySelectorAll("select[name='tierCode']").forEach(select => {
    select.innerHTML = catalog.tiers
      .map(tier => `<option value="${tier.code}">${tier.displayName}</option>`)
      .join("");
  });
}

async function loadCatalog() {
  try {
    const catalog = await api("/api/memberships/catalog");
    state.catalog = catalog;
    renderCatalog(catalog);
    fillSelects(catalog);
    setStatus("API Online", true);
  } catch (error) {
    setStatus("API Error", false);
    showError(error);
  }
}

function showResult(data) {
  elements.rawResult.textContent = JSON.stringify(data, null, 2);
  elements.summary.innerHTML = renderSummary(data);
}

function showError(error) {
  elements.rawResult.textContent = error.message;
  elements.summary.innerHTML = `<div class="empty-state">${error.message}</div>`;
}

function renderSummary(data) {
  if (data.userId && data.plan && data.tier) {
    return `
      <article class="summary-card">
        <h3>User ${data.userId} Membership</h3>
        <div class="summary-grid">
          <div class="metric"><span>Status</span><strong>${data.status}</strong></div>
          <div class="metric"><span>Tier</span><strong>${data.tier.displayName}</strong></div>
          <div class="metric"><span>Plan</span><strong>${data.plan.billingPeriod}</strong></div>
          <div class="metric"><span>Expiry</span><strong>${data.expiresAt}</strong></div>
          <div class="metric"><span>Price</span><strong>${money(data.plan)} ${data.plan.currency}</strong></div>
          <div class="metric"><span>Version</span><strong>${data.version}</strong></div>
        </div>
      </article>
    `;
  }

  if (data.code && data.benefits) {
    return `
      <article class="summary-card">
        <h3>${data.displayName} Eligible</h3>
        <div class="summary-grid">
          <div class="metric"><span>Tier Code</span><strong>${data.code}</strong></div>
          <div class="metric"><span>Rank</span><strong>${data.rankOrder}</strong></div>
          <div class="metric"><span>Benefits</span><strong>${data.benefits.length}</strong></div>
          <div class="metric"><span>Criteria</span><strong>${data.criteria.length || "Base"}</strong></div>
        </div>
      </article>
    `;
  }

  return `<div class="empty-state">Response received.</div>`;
}

function formNumber(form, name) {
  return Number(new FormData(form).get(name));
}

function formValue(form, name) {
  return new FormData(form).get(name);
}

document.querySelectorAll(".tab").forEach(tab => {
  tab.addEventListener("click", () => {
    document.querySelectorAll(".tab").forEach(item => item.classList.remove("active"));
    document.querySelectorAll(".tab-view").forEach(view => view.classList.remove("active"));
    tab.classList.add("active");
    document.querySelector(`[data-view="${tab.dataset.tab}"]`).classList.add("active");
  });
});

elements.subscribeForm.addEventListener("submit", async event => {
  event.preventDefault();
  try {
    const body = {
      userId: formNumber(event.currentTarget, "userId"),
      billingPeriod: formValue(event.currentTarget, "billingPeriod"),
      tierCode: formValue(event.currentTarget, "tierCode")
    };
    showResult(await api("/api/memberships/subscriptions", {
      method: "POST",
      body: JSON.stringify(body)
    }));
  } catch (error) {
    showError(error);
  }
});

elements.loadMembership.addEventListener("click", async () => {
  try {
    const userId = formNumber(elements.manageForm, "userId");
    showResult(await api(`/api/memberships/users/${userId}/subscription`));
  } catch (error) {
    showError(error);
  }
});

elements.manageForm.addEventListener("submit", async event => {
  event.preventDefault();
  try {
    const userId = formNumber(event.currentTarget, "userId");
    const body = { tierCode: formValue(event.currentTarget, "tierCode") };
    showResult(await api(`/api/memberships/users/${userId}/subscription/tier`, {
      method: "PUT",
      body: JSON.stringify(body)
    }));
  } catch (error) {
    showError(error);
  }
});

elements.cancelMembership.addEventListener("click", async () => {
  try {
    const userId = formNumber(elements.manageForm, "userId");
    showResult(await api(`/api/memberships/users/${userId}/subscription`, { method: "DELETE" }));
  } catch (error) {
    showError(error);
  }
});

elements.evaluateForm.addEventListener("submit", async event => {
  event.preventDefault();
  try {
    const form = event.currentTarget;
    const body = {
      userId: formNumber(form, "userId"),
      monthlyOrderCount: formNumber(form, "monthlyOrderCount"),
      monthlyOrderValue: formNumber(form, "monthlyOrderValue"),
      cohorts: []
    };
    showResult(await api("/api/memberships/tiers/evaluate", {
      method: "POST",
      body: JSON.stringify(body)
    }));
  } catch (error) {
    showError(error);
  }
});

loadCatalog();
