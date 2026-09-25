/**
 * Main Application Logic for WardConnect CCMS
 * Integrates with Spring Boot REST API (js/api.js)
 */

function boot() {
  const page = document.body.dataset.page;

  if (page === 'home') initHomePage();
  if (page === 'resident-login') initResidentLogin();
  if (page === 'admin-login') initAdminLogin();
  if (page === 'resident') initResidentDashboard();
  if (page === 'admin') initAdminDashboard();

  initNav();
}

function initNav() {
  const toggle = document.querySelector('.mobile-menu');
  const links = document.querySelector('.nav-links');
  if (toggle && links) {
    toggle.addEventListener('click', () => links.classList.toggle('open'));
  }

  document.querySelectorAll('[data-logout]').forEach(btn => {
    btn.addEventListener('click', () => {
      API.clearSession();
      location.href = 'index.html';
    });
  });

  ensureImageModal();
}

function ensureImageModal() {
  let modal = document.getElementById('image-modal');
  if (!modal) {
    modal = document.createElement('div');
    modal.id = 'image-modal';
    modal.className = 'image-modal hidden';
    modal.setAttribute('aria-hidden', 'true');
    modal.innerHTML = `
      <div class="image-modal-backdrop"></div>
      <div class="image-modal-content">
        <div class="image-modal-header">
          <h3>Attached Resident Photo</h3>
          <button class="image-modal-close" aria-label="Close">&times;</button>
        </div>
        <div class="image-modal-body">
          <img id="modal-img-preview" src="" alt="Complaint attachment photo">
        </div>
      </div>
    `;
    document.body.appendChild(modal);

    const closeBtn = modal.querySelector('.image-modal-close');
    const backdrop = modal.querySelector('.image-modal-backdrop');
    const closeModal = () => modal.classList.add('hidden');
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (backdrop) backdrop.addEventListener('click', closeModal);
    window.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') closeModal();
    });
  }
  return modal;
}

document.addEventListener('click', (e) => {
  const btn = e.target.closest('.view-photo-btn');
  if (btn && btn.dataset.photoSrc) {
    const modal = ensureImageModal();
    const img = modal.querySelector('#modal-img-preview');
    if (img) img.src = btn.dataset.photoSrc;
    modal.classList.remove('hidden');
  }
});

function redirectGuard(role) {
  const s = API.getSession();
  const token = API.getToken();

  if (!s || !token) {
    location.href = role === 'admin' ? 'admin-login.html' : 'resident-login.html';
    return null;
  }

  if (s.role !== role) {
    location.href = s.role === 'admin' ? 'admin-dashboard.html' : 'resident-dashboard.html';
    return null;
  }

  return s;
}

async function initHomePage() {
  try {
    const stats = await API.getPublicStats();
    const statsGrid = document.querySelector('.stats-band .stats-grid');
    if (statsGrid && stats) {
      statsGrid.innerHTML = `
        <div class="stat"><strong>${stats.totalReceived || 0}</strong><span>Complaints received</span></div>
        <div class="stat"><strong>${stats.totalResolved || 0}</strong><span>Issues resolved</span></div>
        <div class="stat"><strong>${stats.totalInProgress || 0}</strong><span>Currently in progress</span></div>
      `;
    }
  } catch (err) {
    console.log('Using default landing stats overview.');
  }
}

function initResidentLogin() {
  const loginForm = document.querySelector('#resident-login-form');
  const signupForm = document.querySelector('#resident-signup-form');

  document.querySelectorAll('[data-switch-form]').forEach(link => {
    link.addEventListener('click', e => {
      e.preventDefault();
      loginForm.classList.toggle('hidden');
      signupForm.classList.toggle('hidden');
      const title = document.querySelector('#auth-title');
      const subtitle = document.querySelector('#auth-subtitle');
      if (title) title.textContent = signupForm.classList.contains('hidden') ? 'Welcome back' : 'Create your resident account';
      if (subtitle) subtitle.textContent = signupForm.classList.contains('hidden') ? 'Sign in to report and follow up on local issues.' : 'Join your neighbours in making the ward better.';
    });
  });

  if (loginForm) {
    loginForm.addEventListener('submit', async e => {
      e.preventDefault();
      const email = loginForm.email.value.trim();
      const password = loginForm.password.value;
      const submitBtn = loginForm.querySelector('button[type="submit"]');

      try {
        setLoading(submitBtn, true, 'Signing in...');
        clearMessage('#resident-login-error');
        await API.loginResident(email, password);
        location.href = 'resident-dashboard.html';
      } catch (err) {
        showMessage('#resident-login-error', err.message);
      } finally {
        setLoading(submitBtn, false, 'Sign in →');
      }
    });
  }

  if (signupForm) {
    signupForm.addEventListener('submit', async e => {
      e.preventDefault();
      const name = signupForm.name.value.trim();
      const email = signupForm.email.value.trim();
      const password = signupForm.password.value;
      const submitBtn = signupForm.querySelector('button[type="submit"]');

      if (password.length < 6) {
        return showMessage('#resident-signup-error', 'Use at least 6 characters for your password.');
      }

      try {
        setLoading(submitBtn, true, 'Creating account...');
        clearMessage('#resident-signup-error');
        await API.registerResident(name, email, password);
        location.href = 'resident-dashboard.html';
      } catch (err) {
        showMessage('#resident-signup-error', err.message);
      } finally {
        setLoading(submitBtn, false, 'Create account →');
      }
    });
  }
}

function initAdminLogin() {
  const form = document.querySelector('#admin-login-form');
  if (form) {
    form.addEventListener('submit', async e => {
      e.preventDefault();
      const email = form.email.value.trim();
      const password = form.password.value;
      const submitBtn = form.querySelector('button[type="submit"]');

      try {
        setLoading(submitBtn, true, 'Authenticating...');
        clearMessage('#admin-login-error');
        await API.loginAdmin(email, password);
        location.href = 'admin-dashboard.html';
      } catch (err) {
        showMessage('#admin-login-error', err.message);
      } finally {
        setLoading(submitBtn, false, 'Enter dashboard →');
      }
    });
  }
}

async function initResidentDashboard() {
  const s = redirectGuard('resident');
  if (!s) return;

  document.querySelectorAll('[data-resident-name]').forEach(el => el.textContent = s.name || 'Resident');

  await renderResidentComplaints();

  const form = document.querySelector('#complaint-form');
  if (form) {
    form.addEventListener('submit', async e => {
      e.preventDefault();
      const title = form.title.value.trim();
      const category = form.category.value;
      const locationValue = form.location.value.trim();
      const urgency = form.urgency ? form.urgency.value : 'Medium';
      const description = form.description.value.trim();
      const submitBtn = form.querySelector('button[type="submit"]');

      if (!title || !category || !locationValue || !description) {
        return showMessage('#complaint-error', 'Please complete all required fields before submitting.');
      }

      try {
        setLoading(submitBtn, true, 'Submitting report...');
        clearMessage('#complaint-error');
        clearMessage('#complaint-success');

        let photoPath = null;
        const photoInput = form.photo;
        if (photoInput && photoInput.files && photoInput.files[0]) {
          const file = photoInput.files[0];
          photoPath = await new Promise((resolve) => {
            const reader = new FileReader();
            reader.onload = (ev) => resolve(ev.target.result);
            reader.onerror = () => resolve(null);
            reader.readAsDataURL(file);
          });
        }

        const created = await API.createComplaint({
          title,
          category,
          location: locationValue,
          priority: urgency,
          description,
          photoPath
        });

        form.reset();
        showMessage('#complaint-success', `Complaint submitted successfully. Your tracking ID is ${created.trackingId || created.id}.`, true);
        await renderResidentComplaints();
      } catch (err) {
        showMessage('#complaint-error', err.message);
      } finally {
        setLoading(submitBtn, false, 'Submit complaint →');
      }
    });
  }
}

async function renderResidentComplaints() {
  const list = document.querySelector('#resident-complaints');
  if (!list) return;

  list.innerHTML = '<div class="empty-state">Loading complaints...</div>';

  try {
    const rows = await API.getMyComplaints();
    if (!rows || !rows.length) {
      list.innerHTML = '<div class="empty-state"><span style="font-size:32px;display:block;margin-bottom:8px">📋</span>You have not submitted any complaints yet. Use the form to report an issue in your ward.</div>';
      return;
    }

    list.innerHTML = rows.map((c, idx) => `
      <article class="complaint-item stagger-item" style="--stagger-index:${idx}">
        <div class="complaint-item-top">
          <div>
            <h3>${escapeHtml(c.title)}</h3>
            <div class="complaint-meta">${escapeHtml(c.trackingId || c.id)} · ${escapeHtml(c.category)} · ${formatDate(c.createdAt)}</div>
          </div>
          ${statusPill(c.status)}
        </div>
        <div class="complaint-meta">${escapeHtml(c.location)} · ${escapeHtml(c.priority || 'Medium')} urgency</div>
        ${c.photoPath ? `<div style="margin:8px 0"><button type="button" class="view-photo-btn btn btn-secondary" data-photo-src="${escapeAttr(c.photoPath)}" style="font-size:12px;padding:5px 12px;display:inline-flex;align-items:center;gap:6px">📷 View Attachment Photo</button></div>` : ''}
        ${c.adminResponse ? `<div class="response"><strong>Ward response:</strong> ${escapeHtml(c.adminResponse)}</div>` : '<div class="response"><strong>Ward response:</strong> Your complaint is in the queue for review.</div>'}
      </article>
    `).join('');
  } catch (err) {
    list.innerHTML = `<div class="form-error">${escapeHtml(err.message)}</div>`;
  }
}

async function initAdminDashboard() {
  const s = redirectGuard('admin');
  if (!s) return;

  await renderAdmin();

  let searchTimeout = null;
  const searchInput = document.querySelector('#admin-search');
  if (searchInput) {
    searchInput.addEventListener('input', () => {
      clearTimeout(searchTimeout);
      searchTimeout = setTimeout(renderAdmin, 300);
    });
  }

  const statusFilter = document.querySelector('#status-filter');
  if (statusFilter) statusFilter.addEventListener('change', renderAdmin);

  const categoryFilter = document.querySelector('#category-filter');
  if (categoryFilter) categoryFilter.addEventListener('change', renderAdmin);

  const complaintsTable = document.querySelector('#complaints-table');
  if (complaintsTable) {
    complaintsTable.addEventListener('change', async e => {
      if (!e.target.matches('[data-status-id]')) return;
      const id = e.target.dataset.statusId;
      const newStatus = e.target.value;
      const respInput = document.querySelector(`[data-response-input="${id}"]`);
      const currentResponse = respInput ? respInput.value.trim() : '';
      const tr = e.target.closest('tr');

      try {
        await API.updateAdminComplaint(id, newStatus, currentResponse);
        if (tr) {
          tr.classList.remove('flash-success');
          void tr.offsetWidth;
          tr.classList.add('flash-success');
        }
        showToast('Status updated');
        await renderAdmin();
      } catch (err) {
        showToast(err.message || 'Failed to update status');
      }
    });

    complaintsTable.addEventListener('click', async e => {
      if (!e.target.matches('.save-response')) return;
      const id = e.target.dataset.responseId;
      const statusSelect = document.querySelector(`[data-status-id="${id}"]`);
      const respInput = document.querySelector(`[data-response-input="${id}"]`);
      
      const currentStatus = statusSelect ? statusSelect.value : 'Pending';
      const newResponse = respInput ? respInput.value.trim() : '';
      const btn = e.target;
      const tr = e.target.closest('tr');

      try {
        btn.disabled = true;
        btn.textContent = 'Saving...';
        await API.updateAdminComplaint(id, currentStatus, newResponse);
        if (tr) {
          tr.classList.remove('flash-success');
          void tr.offsetWidth;
          tr.classList.add('flash-success');
        }
        showToast('Response saved');
        await renderAdmin();
      } catch (err) {
        showToast(err.message || 'Failed to save response');
      } finally {
        if (btn) {
          btn.disabled = false;
          btn.textContent = 'Save';
        }
      }
    });
  }
}

async function renderAdmin() {
  const searchVal = document.querySelector('#admin-search')?.value || '';
  const statusVal = document.querySelector('#status-filter')?.value || 'All';
  const categoryVal = document.querySelector('#category-filter')?.value || 'All';
  const tableBody = document.querySelector('#complaints-table-body');

  try {
    const [filtered, stats] = await Promise.all([
      API.getAdminComplaints(statusVal, categoryVal, searchVal),
      API.getAdminStats()
    ]);

    if (stats) {
      document.querySelector('#count-total').textContent = stats.total || 0;
      document.querySelector('#count-pending').textContent = stats.pending || 0;
      document.querySelector('#count-progress').textContent = stats.inProgress || 0;
      document.querySelector('#count-resolved').textContent = stats.resolved || 0;

      const total = stats.total || 1;
      document.querySelector('#bar-pending').style.height = `${Math.max(10, (stats.pending / total) * 100)}%`;
      document.querySelector('#bar-progress').style.height = `${Math.max(10, (stats.inProgress / total) * 100)}%`;
      document.querySelector('#bar-resolved').style.height = `${Math.max(10, (stats.resolved / total) * 100)}%`;

      document.querySelector('#num-pending').textContent = stats.pending || 0;
      document.querySelector('#num-progress').textContent = stats.inProgress || 0;
      document.querySelector('#num-resolved').textContent = stats.resolved || 0;
    }

    if (tableBody) {
      if (!filtered || !filtered.length) {
        tableBody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><span style="font-size:32px;display:block;margin-bottom:8px">🔍</span>No complaints match the selected filters.</div></td></tr>';
        return;
      }

      tableBody.innerHTML = filtered.map(c => `
        <tr>
          <td>
            <div class="table-title">${escapeHtml(c.title)}</div>
            <div class="table-sub">${escapeHtml(c.trackingId || c.id)} · ${formatDate(c.createdAt)}</div>
          </td>
          <td>
            ${escapeHtml(c.category)}
            <div class="table-sub">${escapeHtml(c.location)}</div>
          </td>
          <td>${escapeHtml(c.priority || 'Medium')}</td>
          <td>
            <select class="table-select" data-status-id="${c.id}">
              <option ${formatStatusStr(c.status) === 'Pending' ? 'selected' : ''}>Pending</option>
              <option ${formatStatusStr(c.status) === 'In Progress' ? 'selected' : ''}>In Progress</option>
              <option ${formatStatusStr(c.status) === 'Resolved' ? 'selected' : ''}>Resolved</option>
            </select>
          </td>
          <td>
            ${c.photoPath ? `
              <button type="button" class="view-photo-btn btn btn-primary" data-photo-src="${escapeAttr(c.photoPath)}" style="font-size:12px;padding:6px 12px;background:var(--red);color:#fff;border-radius:6px;display:inline-flex;align-items:center;gap:5px;font-weight:600;border:none;cursor:pointer">
                🖼️ View Image
              </button>
            ` : `
              <button type="button" class="view-photo-btn btn btn-secondary" data-photo-src="https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?auto=format&fit=crop&w=600&q=80" style="font-size:12px;padding:6px 12px;background:#1f1f23;color:#e5e7eb;border:1px solid var(--dark-border);border-radius:6px;display:inline-flex;align-items:center;gap:5px;font-weight:600;cursor:pointer">
                🖼️ View Image
              </button>
            `}
          </td>
          <td>
            <input class="response-input" data-response-input="${c.id}" value="${escapeAttr(c.adminResponse || '')}" placeholder="Add a response">
            <br>
            <button class="save-response" data-response-id="${c.id}">Save</button>
          </td>
        </tr>
      `).join('');
    }
  } catch (err) {
    if (tableBody) {
      tableBody.innerHTML = `<tr><td colspan="6"><div class="form-error">${escapeHtml(err.message)}</div></td></tr>`;
    }
  }
}

function formatStatusStr(s) {
  if (!s) return 'Pending';
  if (typeof s === 'string') return s;
  return s.value || s.name || 'Pending';
}

function statusPill(status) {
  const statusStr = formatStatusStr(status);
  const cssClass = statusStr === 'Resolved' ? 'resolved' : statusStr === 'In Progress' ? 'progress' : 'pending';
  return `<span class="status ${cssClass}">${escapeHtml(statusStr)}</span>`;
}

function formatDate(d) {
  if (!d) return '';
  const dateObj = new Date(d);
  if (isNaN(dateObj.getTime())) return d;
  return dateObj.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}

function showMessage(selector, text, success = false) {
  const el = document.querySelector(selector);
  if (!el) return;
  el.textContent = text;
  el.className = success ? 'form-success' : 'form-error';
  el.style.display = 'block';
}

function clearMessage(selector) {
  const el = document.querySelector(selector);
  if (!el) return;
  el.textContent = '';
  el.style.display = 'none';
}

function setLoading(btn, isLoading, text) {
  if (!btn) return;
  btn.disabled = isLoading;
  btn.textContent = text;
}

function showToast(text) {
  const el = document.querySelector('#toast');
  if (!el) return;
  el.textContent = text;
  el.classList.add('show');
  setTimeout(() => el.classList.remove('show'), 2200);
}

function escapeHtml(str) {
  return String(str || '').replace(/[&<>'"]/g, c => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    "'": '&#39;',
    '"': '&quot;'
  }[c]));
}

function escapeAttr(str) {
  return escapeHtml(str);
}

document.addEventListener('DOMContentLoaded', boot);
